package pl.trello.dto.response.valid;

import lombok.Builder;
import lombok.Getter;
import org.checkerframework.checker.signature.qual.BinaryNameInUnnamedPackage;

import java.util.List;

@Builder
@Getter
public class GetTaskListsResponseDTO {

    List<GetTaskListResponse> taskLists;
}
