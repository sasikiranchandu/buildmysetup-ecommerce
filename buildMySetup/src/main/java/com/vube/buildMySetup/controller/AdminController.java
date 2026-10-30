	package com.vube.buildMySetup.controller;
	
	import java.io.IOException;
	import java.util.List;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.*;
	import org.springframework.web.multipart.MultipartFile;
	
	import com.vube.buildMySetup.model.Component;
	import com.vube.buildMySetup.service.Componentservice;
	
	@Controller
	@RequestMapping("/admin")
	public class AdminController {
	
	    @Autowired
	    private Componentservice componentService;
	
	    // 👉 1. Show Add Page
	    @GetMapping("/add")
	    public String showAddPage(Model model) {
	        model.addAttribute("component", new Component());
	        return "add-component";
	    }
	
	    // 👉 2. Save Component (with image in DB)
	    @PostMapping("/save")
	    public String saveComponent(@ModelAttribute Component component,
	                                @RequestParam("imageFile") MultipartFile file) {
	
	        try {
	            if (!file.isEmpty()) {
	                component.setImage(file.getBytes()); // ✅ store in DB
	            }
	
	            componentService.addComponent(component);
	
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	
	        return "redirect:/admin/list";
	    }
	
	    // 👉 3. List Components
	    @GetMapping("/list")
	    public String listComponents(Model model) {
	        List<Component> components = componentService.getAllComponents();
	        model.addAttribute("components", components);
	        return "components";
	    }
	
	    // 👉 4. Delete Component
	    @GetMapping("/delete/{id}")
	    public String deleteComponent(@PathVariable Long id) {
	        componentService.deleteComponent(id);
	        return "redirect:/admin/list";
	    }
	
	    // 👉 5. Edit Page
	    @GetMapping("/edit/{id}")
	    public String editComponent(@PathVariable Long id, Model model) {
	        Component component = componentService.getComponentById(id);
	        model.addAttribute("component", component);
	        return "edit-component";
	    }
	
	    // 👉 6. Update Component
	    @PostMapping("/update")
	    public String updateComponent(@ModelAttribute Component component,
	                                  @RequestParam("imageFile") MultipartFile file) {
	
	        try {
	            if (!file.isEmpty()) {
	                component.setImage(file.getBytes()); // update image
	            } else {
	                // keep old image if new one not uploaded
	                Component existing = componentService.getComponentById(component.getId());
	                component.setImage(existing.getImage());
	            }
	
	            componentService.updateComponent(component);	    
	            } catch (IOException e) {
	            e.printStackTrace();
	        }
	
	        return "redirect:/admin/list";
	    }
	}