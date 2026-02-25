package ru.itmo.is.service;

import ru.itmo.is.dao.ImportHistoryDAO;
import ru.itmo.is.entity.ImportHistory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class ImportHistoryService implements Serializable {
    @Inject
    private ImportHistoryDAO importHistoryDAO;

    public ImportHistory createHistory(ImportHistory history) {
        return importHistoryDAO.create(history);
    }

    public ImportHistory updateHistory(ImportHistory history) {
        return importHistoryDAO.update(history);
    }

    public List<ImportHistory> getAllHistory() {
        return importHistoryDAO.findAll();
    }

    public List<ImportHistory> getHistoryByUsername(String username) {
        return importHistoryDAO.findByUsername(username);
    }
}
