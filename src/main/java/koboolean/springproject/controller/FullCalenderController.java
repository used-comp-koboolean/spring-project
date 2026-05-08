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

    @GetMapping("/api/full/{year}")
    public ResponseEntity<List<CalEvent>> list(@PathVariable String year){

        List<CalEvent> events = fullCalenderService.list(year);

        return ResponseEntity.ok(events);
    }
}
