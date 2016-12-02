package app.validator;

import app.domain.CurrencyConversionsData;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CurrencyConverterFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CurrencyConversionsData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CurrencyConversionsData form = (CurrencyConversionsData) target;
        validateAmount(form, errors);
    }

    private void validateAmount(CurrencyConversionsData form, Errors errors) {
        if (form.getAmount() <= 0) {
            errors.rejectValue("amount", "amount.invalid");
        }
    }
}