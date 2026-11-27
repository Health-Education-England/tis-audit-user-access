/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uk.nhs.tis.audit.useraccess.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.nhs.tis.audit.useraccess.config.SecurityConfig;
import uk.nhs.tis.audit.useraccess.dto.LastAccessDto;

/**
 * Tests for {@link LogsResource}.
 * <p>They currently test framework usage more than the resource itself.  As the resource is developed,
 * they should be re-evaluated and likely replaced with tests of our own code.
 */
@WebMvcTest(LogsResource.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class LogsResourceTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoSpyBean
  private LogsResource logsResource;

  @MockitoBean
  private JwtDecoder jwtDecoder;

  @Test
  void shouldUseRequestedPageAndReturnArray() throws Exception {
    doReturn(List.of(new LastAccessDto("user@tis.nhs.uk",
        OffsetDateTime.parse("2027-04-01T12:34:56Z"))))
        .when(logsResource).getAll(any(Pageable.class));
    mockMvc.perform(get("/api/logs").param("page", "0").param("size", "1")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_read:all:tbd"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].email").value("user@tis.nhs.uk"))
        .andExpect(jsonPath("$[0].accessDateTime").value("2027-04-01T12:34:56Z"));

    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
    verify(logsResource).getAll(captor.capture());
    assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
    assertThat(captor.getValue().getPageSize()).isEqualTo(1);
  }

  @Test
  void shouldRejectUnauthenticatedRequests() throws Exception {
    mockMvc.perform(get("/api/logs"))
        .andExpect(status().isUnauthorized());
    mockMvc.perform(post("/api/logs").contentType(MediaType.APPLICATION_JSON).content("[]"))
        .andExpect(status().isUnauthorized());
    verify(logsResource, never()).getAll(any(Pageable.class));
    verify(logsResource, never()).saveLogs(any());
  }

  @Test
  void shouldRejectRequestsWithoutRequiredScope() throws Exception {
    mockMvc.perform(get("/api/logs")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_write:all:tbd"))))
        .andExpect(status().isForbidden());
    mockMvc.perform(post("/api/logs").contentType(MediaType.APPLICATION_JSON).content("[]")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_read:all:tbd"))))
        .andExpect(status().isForbidden());
    verify(logsResource, never()).getAll(any(Pageable.class));
    verify(logsResource, never()).saveLogs(any());
  }

  @Test
  void shouldAllowSaveWithWriteScope() throws Exception {
    doReturn("saved").when(logsResource).saveLogs(any());
    mockMvc.perform(post("/api/logs").contentType(MediaType.APPLICATION_JSON).content("[]")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_write:all:tbd"))))
        .andExpect(status().isOk());
    verify(logsResource).saveLogs(List.of());
  }

  @Test
  void shouldThrowUnsupportedOperationExceptionForGetAll() {
    assertThrows(UnsupportedOperationException.class, () -> {
      logsResource.getAll(Pageable.ofSize(1));
    });
  }

  @Test
  void shouldThrowUnsupportedOperationExceptionForSave() {
    assertThrows(UnsupportedOperationException.class, () -> {
      logsResource.saveLogs(List.of());
    });
  }
}
