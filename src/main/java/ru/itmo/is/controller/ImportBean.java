package ru.itmo.is.controller;

import org.primefaces.model.file.UploadedFile;
import ru.itmo.is.entity.ImportHistory;
import ru.itmo.is.service.ImportHistoryService;
import ru.itmo.is.service.ImportService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ImportBean implements Serializable {
    @Inject
    private ImportService importService;

    @Inject
    private ImportHistoryService importHistoryService;

    @Inject
    private AuthBean authBean;

    private UploadedFile file;

    public void upload() {
        if (file == null) {
            addMessage("Please select a file");
            return;
        }

        ImportHistory history = new ImportHistory();
        history.setUsername(authBean.getCurrentUser().getUsername());
        history.setFilename(file.getFileName());
        history.setStatus("PROCESSING");

        history = importHistoryService.createHistory(history);

        try {
            int count = importService.importTickets(file.getInputStream());
            history.setStatus("SUCCESS");
            history.setAddedCount(count);
            addMessage("Successfully imported " + count + " tickets");
        } catch (Exception e) {
            history.setStatus("FAILED");
            history.setErrorMessage(e.getMessage());
            addMessage("Import failed: " + e.getMessage());
        } finally {
            importHistoryService.updateHistory(history);
        }
    }

    private void addMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
}
