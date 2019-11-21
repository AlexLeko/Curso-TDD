package matchers;

import br.ce.alexleko.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {
    // O valor generic <Date> é o primeiro parametro do assetThat que invoca esse Matcher;


    private Integer diaSemana;

    // o parametro é o segundo parametro(Tipo) do assetThat que invoca esse Matcher;
    public DiaSemanaMatcher(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    // Configura a mensagem de exception
    public void describeTo(Description description) {
        Calendar data = Calendar.getInstance();
        data.set(Calendar.DAY_OF_WEEK, diaSemana);

        String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
        description.appendText(dataExtenso);
    }

    // Tipo <Date> tem que ser igual o do generic do extends;
    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.verificarDiaSemana(data, diaSemana);
    }
}
