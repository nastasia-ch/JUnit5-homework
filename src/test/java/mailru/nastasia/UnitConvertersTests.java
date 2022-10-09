package mailru.nastasia;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
        void setUp() {
        open("https://www.unitconverters.net/");
        Configuration.holdBrowserOpen = true;
    }

    @ValueSource(strings = {
            "Length",
            "Temperature",
            "Area",
            "Volume",
            "Weight",
            "Time"
    })
    @DisplayName("Is there unit converter section on page")
    @ParameterizedTest
    void isThereUnitConverterSectionOnPage(String testData) {
        $("#menu").shouldHave(text(testData));
    }

    @CsvSource(value = {
            "Length,Kilometer,Mile,150,93.205331345",
            "Temperature,Celsius,Fahrenheit,36,96.8",
            "Volume,Liter,US Gallon,55,14.529469727"
    })
    @DisplayName("Check conversion one unit to another")
    @ParameterizedTest
    void checkConversionOneUnitToAnother(String unitName,
                           String firstUnit,
                           String secondUnit,
                           String value,
                           String result) {
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
        $("#menu").$(byText(unit)).click();
        $("#calFrom").$$("option").
                shouldHave(CollectionCondition.texts(valuesOfUnit));
        $("#calTo").$$("option").
                shouldHave(CollectionCondition.texts(valuesOfUnit));
    }
}
