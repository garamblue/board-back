package com.ganacom.board_back.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ganacom.board_back.dto.JoinDTO;
import com.ganacom.board_back.service.implement.JoinService;

@Controller
@ResponseBody
public class MainController {
    // log analysis //
    private static final Log logger = LogFactory.getLog(MainController.class);

    private final JoinService joinService;

    public MainController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public String joinProcess(JoinDTO joinDTO) {
        logger.debug("joinProcess: " + joinDTO);
        joinService.joinProcess(joinDTO);
        return "ok";
    }

    @GetMapping("/main")
    public String mainP() {
        return "main Controller";
    }

}