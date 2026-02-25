package ru.itmo.is.controller;

import ru.itmo.is.entity.ImportHistory;
import ru.itmo.is.service.ImportHistoryService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ImportHistoryBean implements Serializable {
    @Inject
    private ImportHistoryService importHistoryService;

    @Inject
    private AuthBean authBean;

    private List<ImportHistory> history;

    @PostConstruct
    public void init() {
        loadHistory();
    }

    public void loadHistory() {
        if (authBean.isAdmin()) {
            history = importHistoryService.getAllHistory();
        } else if (authBean.isLoggedIn()) {
            history = importHistoryService.getHistoryByUsername(authBean.getCurrentUser().getUsername());
        }
    }

    public List<ImportHistory> getHistory() {
        return history;
    }
}
