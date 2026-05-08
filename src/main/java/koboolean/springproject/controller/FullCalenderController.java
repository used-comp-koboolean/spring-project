package koboolean.springproject.controller;

import koboolean.springproject.domains.dto.CalEvent;
import koboolean.springproject.service.FullCalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FullCalenderController {

    private final FullCalenderService fullCalenderService;

    @GetMapping("/api/full/{month}")
    public ResponseEntity<List<CalEvent>> list(@PathVariable String month){

        List<CalEvent> events = fullCalenderService.list();

        return ResponseEntity.ok(events);
    }
}
