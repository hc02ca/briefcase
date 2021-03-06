package org.opendatakit.briefcase.model.form;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.Test;
import org.opendatakit.briefcase.pull.aggregate.Cursor;

public class FormMetadataTest {
  private static final OffsetDateTime SOME_DATE_TIME = OffsetDateTime.parse("2019-01-01T00:00:00.000Z");
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Test
  public void can_be_serialized_and_deserialized_into_and_from_JSON() {
    Path storageRoot = Paths.get("/some/path");
    Stream.of(
        FormKey.of("Some form", "some-form"),
        FormKey.of("Some form", "some-form", "some numeric version")
    ).flatMap(key -> Stream.of(
        new FormMetadata(key, storageRoot, Paths.get("forms/Some form"), true, Cursor.empty(), Optional.empty()),
        new FormMetadata(key, storageRoot, Paths.get("forms/Some form"), false, Cursor.empty(), Optional.empty()),
        new FormMetadata(key, storageRoot, Paths.get("forms/Some form"), true, Cursor.from("some cursor"), Optional.empty()),
        new FormMetadata(key, storageRoot, Paths.get("forms/Some form"), false, Cursor.from("some cursor"), Optional.empty()),
        new FormMetadata(key, storageRoot, Paths.get("forms/Some form"), true, Cursor.empty(), Optional.of(new SubmissionExportMetadata("some instance ID", SOME_DATE_TIME, SOME_DATE_TIME))),
        new FormMetadata(key, storageRoot, Paths.get("forms/Some form"), false, Cursor.empty(), Optional.of(new SubmissionExportMetadata("some instance ID", SOME_DATE_TIME, SOME_DATE_TIME))),
        new FormMetadata(key, storageRoot, Paths.get("forms/Some form"), true, Cursor.from("some cursor"), Optional.of(new SubmissionExportMetadata("some instance ID", SOME_DATE_TIME, SOME_DATE_TIME))),
        new FormMetadata(key, storageRoot, Paths.get("forms/Some form"), false, Cursor.from("some cursor"), Optional.of(new SubmissionExportMetadata("some instance ID", SOME_DATE_TIME, SOME_DATE_TIME)))
    )).forEach(formMetadata -> assertThat(FormMetadata.from(storageRoot, formMetadata.asJson(MAPPER)), is(formMetadata)));
  }

}
