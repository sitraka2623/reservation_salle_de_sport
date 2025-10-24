package com.gestion.sallesport.controller;

import com.gestion.sallesport.entity.Reservation;
import com.gestion.sallesport.entity.Salle;
import com.gestion.sallesport.service.DashboardService;
import com.gestion.sallesport.service.ReservationService;
import com.gestion.sallesport.service.SalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ReservationService reservationService;
    private final SalleService salleService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Stats cards
        model.addAttribute("totalClients", dashboardService.countClients());
        model.addAttribute("totalSalles", dashboardService.countSalles());
        model.addAttribute("totalReservations", dashboardService.countReservations());
        model.addAttribute("revenueMonth", dashboardService.revenueMonth());

        // Recent Reservations (les 5 dernières)
        List<Reservation> recentReservations = reservationService.getAllReservations()
                .stream()
                .sorted(Comparator.comparing(Reservation::getDateDebut).reversed())
                .limit(5)
                .toList();
        model.addAttribute("recentReservations", recentReservations);

        // Popular Salles (top 5 par nombre de réservations)
        List<Salle> popularSalles = salleService.getAllSalles()
                .stream()
                .sorted(Comparator.comparingInt(s -> -reservationService.getReservationsBySalle(s.getId()).size()))
                .limit(5)
                .map(s -> {
                    int count = reservationService.getReservationsBySalle(s.getId()).size();
                    double occupancyRate = s.getCapacite() > 0 ? (count * 100.0 / s.getCapacite()) : 0;
                    s.setOccupancyRate(occupancyRate);
                    return s;
                })
                .toList();
        model.addAttribute("popularSalles", popularSalles);

        return "dashboard";
    }
}
