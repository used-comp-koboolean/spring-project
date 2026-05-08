package koboolean.springproject.service;

import koboolean.springproject.domains.dto.CalEvent;

import java.util.List;

public interface FullCalenderService {
    List<CalEvent> list();
}
