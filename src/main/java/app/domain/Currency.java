package app.domain;

public enum Currency {
    RUB("Russian ruble", "Российский рубль"),
    MDL("Moldova Lei", "Молдавский лей"),
    USD("US Dollar", "Доллар США"),
    ZAR("S.African Rand", "Южноафриканский рэнд"),
    TMT("New Turkmenistan Manat", "Новый туркменский манат"),
    TJS("Tajikistan Ruble", "Таджикский сомони"),
    AUD("Australian Dollar", "Австралийский доллар"),
    AZN("Azerbaijan Manat", "Азербайджанский манат"),
    GBP("British Pound Sterling", "Фунт стерлингов Соединенного королевства"),
    AMD("Armenia Dram", "Армянский драм"),
    BYN("Belarussian Ruble", "Белорусский рубль"),
    BGN("Bulgarian lev", "Болгарский лев"),
    BRL("Brazil Real", "Бразильский реал"),
    HUF("Hungarian Forint", "Венгерский форинт"),
    DKK("Danish Krone", "Датская крона"),
    EUR("Euro", "Евро"),
    INR("Indian Rupee", "Индийская рупия"),
    KZT("Kazakhstan Tenge", "Казахстанский тенге"),
    CAD("Canadian Dollar", "Канадский доллар"),
    KGS("Kyrgyzstan Som", "Киргизский сом"),
    CNY("China Yuan", "Китайский юань"),
    NOK("Norwegian Krone", "Норвежская крона"),
    PLN("Polish Zloty", "Польский злотый"),
    RON("Romanian Leu", "Румынский лей"),
    XDR("SDR", "СДР (специальные права заимствования)"),
    SGD("Singapore Dollar", "Сингапурский доллар"),
    TRY("Turkish Lira", "Турецкая лира"),
    UZS("Uzbekistan Sum", "Узбекский сум"),
    UAH("Ukrainian Hryvnia", "Украинская гривна"),
    CZK("Czech Koruna", "Чешская крона"),
    SEK("Swedish Krona", "Шведская крона"),
    CHF("Swiss Franc", "Швейцарский франк"),
    KRW("South Korean Won", "Вон Республики Корея"),
    JPY("Japanese Yen", "Японская иена");

    private final String engName;
    private final String ruName;

    Currency(String engName, String ruName) {
        this.engName = engName;
        this.ruName = ruName;
    }

    public String getRuName() {
        return ruName;
    }

    public String getEngName() {
        return engName;
    }
}
