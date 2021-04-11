package me.hoon.springrestapiwithtdd.events;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(
        @RequestBody @Valid EventDto eventDto,
        Errors errors
    ) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class); // DTO to Entity
        event.update();

        Event createdEvent = eventRepository.save(event);

        URI uri = linkTo(EventController.class).slash(createdEvent.getId()).toUri();

        /* 자바 빈 스팩을 준수하기 떄문에 json으로 serializer 됨 -> ObjectMapper가 BeanSerializer를 사용하여 작업 수행 */
        return ResponseEntity.created(uri).body(createdEvent);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> events = eventRepository.findAll(pageable);
        PagedModel<EventResource> eventResources = assembler.toModel(events, EventResource::new);

        return ResponseEntity.ok(eventResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        EventResource body = new EventResource(event);

        return ResponseEntity.ok(body);

    }
}
