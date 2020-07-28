package pl.dmichalski.reservations.business.ui.forms.address.controller;

import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.dmichalski.reservations.business.domain.entity.address.AddressEntity;
import pl.dmichalski.reservations.business.service.address.AddressService;
import pl.dmichalski.reservations.business.ui.forms.address.model.AddressTableModel;
import pl.dmichalski.reservations.business.ui.forms.address.view.AddressTableBtnPanel;
import pl.dmichalski.reservations.business.ui.forms.address.view.AddressTableFrame;
import pl.dmichalski.reservations.business.ui.forms.address.view.modal.AddAddressFrame;
import pl.dmichalski.reservations.business.ui.forms.address.view.modal.AddressFormBtnPanel;
import pl.dmichalski.reservations.business.ui.forms.address.view.modal.AddressFormPanel;
import pl.dmichalski.reservations.business.ui.shared.controller.AbstractFrameController;
import pl.dmichalski.reservations.business.util.constant.ConstMessagesEN;
import pl.dmichalski.reservations.business.util.notifications.Notifications;
import pl.dmichalski.reservations.business.validation.AddressValidator;
import pl.dmichalski.reservations.business.validation.ValidationError;

@Controller
@AllArgsConstructor
public class AddressController extends AbstractFrameController {

    private final AddressTableFrame tableFrame;
    private final AddAddressFrame addFrame;
    private final AddressTableModel tableModel;
    private final AddressService addressService;
    private final AddressValidator validator;

    @PostConstruct
    private void prepareListeners() {
        AddressTableBtnPanel tableBtnPanel = tableFrame.getTableBtnPanel();
        AddressFormBtnPanel formBtnPanel = addFrame.getFormBtnPanel();

        registerAction(tableBtnPanel.getAddBtn(), (e) -> showAddModal());
        registerAction(tableBtnPanel.getRemoveBtn(), (e) -> removeEntity());
        registerAction(formBtnPanel.getSaveBtn(), (e) -> saveEntity());
        registerAction(formBtnPanel.getCancelBtn(), (e) -> closeModalWindow());
    }

    @Override
    public void prepareAndOpenFrame() {
        loadEntities();
        showTableFrame();
    }

    private void loadEntities() {
        List<AddressEntity> entities = addressService.findAll();
        tableModel.clear();
        tableModel.addEntities(entities);
    }

    private void showTableFrame() {
        tableFrame.setVisible(true);
    }

    private void showAddModal() {
        addFrame.setVisible(true);
    }

    private void saveEntity() {
        AddressFormPanel formPanel = addFrame.getFormPanel();
        AddressEntity entity = formPanel.getEntityFromForm();
        Optional<ValidationError> errors = validator.validate(entity);
        if (errors.isPresent()) {
            ValidationError validationError = errors.get();
            Notifications.showFormValidationAlert(validationError.getMessage());
        } else {
            addressService.save(entity);
            tableModel.addEntity(entity);
            closeModalWindow();
        }
    }

    private void closeModalWindow() {
        addFrame.getFormPanel().clearForm();
        addFrame.dispose();
    }

    private void removeEntity() {
        try {
            JTable clientTable = tableFrame.getTablePanel().getTable();
            int selectedRow = clientTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(null,
                        ConstMessagesEN.Messages.NON_ROW_SELECTED,
                        ConstMessagesEN.Messages.ALERT_TILE,
                        JOptionPane.ERROR_MESSAGE);
            } else {
                AddressEntity entity = tableModel.getEntityByRow(selectedRow);
                addressService.remove(entity);
                tableModel.removeRow(selectedRow);
            }
        } catch (Exception e) {
            Notifications.showDeleteRowErrorMessage();
        }
    }

}
