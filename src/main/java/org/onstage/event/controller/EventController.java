package org.onstage.event.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onstage.event.client.Event;
import org.onstage.event.client.EventOverview;
import org.onstage.event.model.EventEntity;
import org.onstage.event.model.mappers.EventMapper;
import org.onstage.event.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("events")
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventService service;
    private final EventMapper mapper;

    @GetMapping("/{id}")
    public Event getById(@PathVariable final String id) {
        return mapper.toApi(service.getById(id));
    }

    @GetMapping
    public List<EventOverview> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate) {
        if (startDate != null || endDate != null) {
            return mapper.toOverviewList(service.getAllByRange(startDate, endDate));
        }
        return mapper.toOverviewList(service.getAll(search));
    }

    @PostMapping
    public String create(@RequestBody() EventEntity event) {
        return service.create(event).id();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable final String id) {
        return service.delete(id);
    }

    @PatchMapping("/{id}")
    public Event patch(@PathVariable String id, @RequestBody JsonPatch jsonPatch) {
        return mapper.toApi(service.patch(id, jsonPatch));
    }
}
