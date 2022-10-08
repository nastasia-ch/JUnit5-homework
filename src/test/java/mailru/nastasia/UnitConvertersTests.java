package mailru.nastasia;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class UnitConvertersTests {

    @ValueSource(strings = {
            "Length",
            "Temperature",
            "Area",
            "Volume",
            "Weight",
            "Time"
    })
    @DisplayName("Are there all units converts")
    @ParameterizedTest
    void areThereAllUnitConverters(String testData) {
        open("https://www.unitconverters.net/");
        Configuration.holdBrowserOpen = true;
        $("#menu").shouldHave(text(testData));
    }

    @CsvSource(value = {
            "Length,Kilometer,Mile,150,93.205331345",
            "Temperature,Celsius,Fahrenheit,36,96.8",
            "Volume,Liter,US Gallon,55,14.529469727"
    })
    @DisplayName("Length convert test")
    @ParameterizedTest
    void lengthConvertTest(String unitName,
                           String firstUnit,
                           String secondUnit,
                           String value,
                           String result) {
        open("https://www.unitconverters.net/");
        Configuration.holdBrowserOpen = true;
        $("#menu").$(byText(unitName)).click();
        $("#calFrom").selectOption(firstUnit);
        $("#calTo").selectOption(secondUnit);
        $("#fromVal").setValue(value);
        $("[name=toVal]").shouldHave(value(result));
        $("#calResults").shouldHave(text(value + " " + firstUnit + " = " + result + " " + secondUnit));
    }

    static Stream<Arguments> checkListsOfUnits() {
        return Stream.of(
                Arguments.of("Length",List.of("Meter","Kilometer","Centimeter","Millimeter",
                        "Micrometer","Nanometer","Mile","Yard","Foot","Inch","Light Year")),
                Arguments.of("Temperature",List.of("Celsius","Kelvin","Fahrenheit")),
                Arguments.of("Area",List.of("Square Meter","Square Kilometer",
                        "Square Centimeter","Square Millimeter","Square Micrometer","Hectare",
                        "Square Mile","Square Yard","Square Foot","Square Inch","Acre"))
        );
    }
    @MethodSource()
    @DisplayName("Check lists of units")
    @ParameterizedTest
    void checkListsOfUnits(String unit, List<String> valuesOfUnit) {
        open("https://www.unitconverters.net/");
        Configuration.holdBrowserOpen = true;
        $("#menu").$(byText(unit)).click();
        $("#calFrom").$$("option").
                shouldHave(CollectionCondition.texts(valuesOfUnit));
        $("#calTo").$$("option").
                shouldHave(CollectionCondition.texts(valuesOfUnit));
    }
}
