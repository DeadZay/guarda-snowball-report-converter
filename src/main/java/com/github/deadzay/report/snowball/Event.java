package com.github.deadzay.report.snowball;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Event {
    Buy("Покупка"),
    Sell("Продажа"),
    Dividend("Дивиденд или купон"),
    Stock_As_Dividend("Дивиденд в виде акции"),
    Split("Сплит"),
    Spinoff("Спинофф"),
    Fee("Комиссия"),
    Amortisation("Амортизация по облигации"),
    Repayment("Полное погашение облигации"),
    Cash_In("Пополнение счета"),
    Cash_Out("Вывод средств со счета"),
    Cash_Gain("Прочие доходы в валюте (например, возврат налогов)"),
    Cash_Expense("Прочие расходы в валюте (например, налоги на прирост капитала)"),
    Cash_Convert("Конвертация, или покупка и продажа валюты, см. пример такой записи внизу"),
    Active_Convert("Конвертация одного актива в другой. Подробности в поле Note");

    private final String description;

}
