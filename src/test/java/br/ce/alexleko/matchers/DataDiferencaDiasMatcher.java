package br.ce.alexleko.matchers;

import br.ce.alexleko.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

    private Integer dias;

    public DataDiferencaDiasMatcher(Integer dias) {
        this.dias = dias;
    }

    @Override
    public void describeTo(Description description) {
        // Formatação da Data para deixar legivel o erro no console.
        Date dataEsperada = DataUtils.obterDataComDiferencaDias(dias);
        DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
        description.appendText(format.format(dataEsperada));
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(dias));
    }


}
