package com.vube.buildMySetup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vube.buildMySetup.model.Component;
import com.vube.buildMySetup.service.Componentservice;

@Controller
public class ComponentController {

    @Autowired
    private Componentservice componentService;

    // ✅ COMBINED METHOD (ALL + SEARCH)
    @GetMapping("/components")
    public String showComponents(@RequestParam(required = false) String keyword,
                                 Model model) {

        List<Component> list;

        if (keyword != null && !keyword.isEmpty()) {
            list = componentService.searchComponent(keyword); // 🔍 search
        } else {
            list = componentService.getAllComponents(); // 📦 all
        }

        model.addAttribute("components", list);
        model.addAttribute("keyword", keyword);

        return "components";
    }

    // ✅ IMAGE API
    @GetMapping("/component/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {

        Component c = componentService.getComponentById(id);

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(c.getImage());
    }

    // ✅ DETAIL PAGE
    @GetMapping("/component/{id}")
    public String showComponentDetail(@PathVariable Long id, Model model) {
        model.addAttribute("c", componentService.getComponentById(id));
        return "component-detail";
    }
}