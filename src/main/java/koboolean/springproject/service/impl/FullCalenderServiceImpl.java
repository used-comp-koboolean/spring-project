package koboolean.springproject.service.impl;


import koboolean.springproject.domains.dto.CalEvent;
import koboolean.springproject.domains.enums.CalType;
import koboolean.springproject.service.FullCalenderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FullCalenderServiceImpl implements FullCalenderService {


    @Override
    public List<CalEvent> list() {

        List<CalEvent> events = new ArrayList<>();

        events.add(
                CalEvent.builder()
                        .id(1L)
                        .title("TEST_" + 1)
                        .start("202605" + StringUtils.leftPad(String.valueOf(1 + 1), 2, '0'))
                        .end("202605" + StringUtils.leftPad(String.valueOf(1 + 5), 2, '0'))
                        .allDay(true)
                        .type(CalType.TYPE1)
                        .build()
        );

        return events;
    }
}
