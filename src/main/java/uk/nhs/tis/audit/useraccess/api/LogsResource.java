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

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.nhs.tis.audit.useraccess.dto.LastAccessDto;

/**
 * REST controller for managing logs of user access.
 */
@RestController
@RequestMapping("/api/logs")
@Slf4j
public class LogsResource {

  /**
   * Retrieves a page of last access information.
   *
   * @param pageable page size and number to get
   * @return the last access information in the requested page
   */
  @GetMapping
  public List<LastAccessDto> getAll(Pageable pageable) {
    log.debug("Attempting to get logs with page {}", pageable);
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Persists a collection of last access information. Clients should not rely on request objects
   * being persisted as new or overwriting existing data.
   *
   * @param logs the tuples of email and last access date time to persist
   * @return !EXPERIMENTAL a message which MAY provide more detail for the action.
   */
  @PostMapping
  public String saveLogs(@RequestBody List<LastAccessDto> logs) {
    log.debug("Request made to upsert {} logs", logs.size());
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
