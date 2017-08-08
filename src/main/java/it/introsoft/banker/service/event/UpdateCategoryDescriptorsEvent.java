package it.introsoft.banker.service.event;

import it.introsoft.banker.model.raw.Bank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jopendocument.dom.spreadsheet.Sheet;

@AllArgsConstructor
@Builder
@Getter
public class UpdateCategoryDescriptorsEvent {

    private final Bank bank;

    private final Sheet sheet;

}
