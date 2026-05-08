package koboolean.springproject.domains.dto;

import koboolean.springproject.domains.enums.CalType;
import lombok.Builder;

@Builder
public record CalEvent(Long id, String title, String start, String end, Boolean allDay, CalType type) {
}
