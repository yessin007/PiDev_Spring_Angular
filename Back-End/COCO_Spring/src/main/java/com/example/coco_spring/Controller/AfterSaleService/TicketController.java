package com.example.coco_spring.Controller.AfterSaleService;

import com.example.coco_spring.Entity.Technician;
import com.example.coco_spring.Entity.Ticket;
import com.example.coco_spring.Repository.TechnicianRepository;
import com.example.coco_spring.Repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ticket")

public class TicketController {
    TicketRepository ticketRepository ;
    TechnicianRepository technicianRepository ;


    @PostMapping("/assign/{ticketId}/{technicianId}")
    public ResponseEntity<Ticket> assignTicket(@PathVariable Long ticketId, @PathVariable Long technicianId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        Optional<Technician> optionalTechnician = technicianRepository.findById(technicianId);

        if (!optionalTicket.isPresent() || !optionalTechnician.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Ticket ticket = optionalTicket.get();
        Technician technician = optionalTechnician.get();
        ticket.setTechnician(technician);

        ticketRepository.save(ticket);

        return ResponseEntity.ok(ticket);
    }
    @GetMapping("/{technicianId}/tickets")
    public ResponseEntity<List<Ticket>> getAssignedTickets(@PathVariable Long technicianId) {
        Optional<Technician> optionalTechnician = technicianRepository.findById(technicianId);

        if (!optionalTechnician.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Technician tt = optionalTechnician.get();
        List<Ticket> assignedTickets = ticketRepository.findByTechnicianName(tt.getName());

        return ResponseEntity.ok(assignedTickets);
    }
    @GetMapping("/tickets/average-response-time")
    public ResponseEntity<Double> getAverageResponseTime() {
        // R??cup??ration des donn??es des tickets ?? partir de la base de donn??es
        List<Ticket> tickets = ticketRepository.findAll();
        double[] responseTimes = tickets.stream().mapToDouble(Ticket::getResponseTime).toArray();

        // Calcul du temps de r??ponse moyen des tickets
        DescriptiveStatistics stats = new DescriptiveStatistics(responseTimes);
        double averageResponseTime = stats.getMean();

        // Envoi de la r??ponse avec le temps de r??ponse moyen
        return ResponseEntity.ok(averageResponseTime);
    }
    // ...

    @GetMapping("/tickets/average-resolution-time")
    public ResponseEntity<Double> getAverageResolutionTime() {
        // R??cup??ration des donn??es des tickets ?? partir de la base de donn??es
        List<Ticket> tickets = ticketRepository.findAll();

        // Calcul de la dur??e moyenne de r??solution des probl??mes
        long totalResolutionTime = 0;
        int numResolvedTickets = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getStatus().equals("R??solu")) {
                LocalDateTime openDateTime = ticket.getOpenDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime closeDateTime = ticket.getCloseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                Duration duration = Duration.between(openDateTime, closeDateTime);
                totalResolutionTime += duration.getSeconds();
                numResolvedTickets++;
            }
        }
        double averageResolutionTime = (double) totalResolutionTime / numResolvedTickets;

        // Envoi de la r??ponse avec la dur??e moyenne de r??solution des probl??mes
        return ResponseEntity.ok(averageResolutionTime);
    }
    @GetMapping("/tickets/most-common-problems")
    public ResponseEntity<List<String>> getMostCommonProblems() {
        // R??cup??ration des donn??es des tickets ?? partir de la base de donn??es
        List<Ticket> tickets = ticketRepository.findAll();

        // Comptage des occurrences de chaque probl??me
        Map<String, Integer> problemCounts = new HashMap<>();
        for (Ticket ticket : tickets) {
            String problem = ticket.getSubject();
            int count = problemCounts.getOrDefault(problem, 0);
            problemCounts.put(problem, count + 1);
        }

        // Tri des probl??mes par ordre d??croissant de leur nombre d'occurrences
        List<Map.Entry<String, Integer>> sortedProblemCounts = new ArrayList<>(problemCounts.entrySet());
        sortedProblemCounts.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // S??lection des probl??mes les plus courants
        List<String> mostCommonProblems = new ArrayList<>();
        for (int i = 0; i < 5 && i < sortedProblemCounts.size(); i++) {
            String problem = sortedProblemCounts.get(i).getKey();
            mostCommonProblems.add(problem);
        }

        // Envoi de la r??ponse avec les probl??mes les plus courants
        return ResponseEntity.ok(mostCommonProblems);
    }

    @PutMapping("updateTicketStatus/{id}")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable Long id, @RequestParam String status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Ticket not found with id " + id));
        ticket.setStatus(status);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    @GetMapping("/getTicketsByStatus")
    public ResponseEntity<List<Ticket>> getTicketsByStatus(@RequestParam String status) {
        List<Ticket> tickets = ticketRepository.findByStatus(status);
        return ResponseEntity.ok(tickets);
    }
}

