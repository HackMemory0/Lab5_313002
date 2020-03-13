package Managers;

import Collection.City;
import Collection.Coordinates;
import Collection.Government;
import Collection.Human;
import Exceptions.InvalidValueException;
import Utils.NumUtil;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Консольный менеджер
 */
public class ConsoleManager {

    private Scanner scanner;
    private boolean isScript;

    public ConsoleManager(Reader reader, boolean isScript)
    {
        scanner = new Scanner(reader);
        this.isScript = isScript;
    }

    public void writeln(String msg){
        System.out.println(msg);
    }

    public void write(String msg){ System.out.print(msg); }


    /**
     * выводит сообщение с вводом от пользователя
     * @param message
     * @param canNull
     * @return
     */
    public String readWithMessage(String message, boolean canNull) {
        String output = "";

        do {
            if (output == null) {
                writeln("Вы ввели пустую строку, когда ожидалось не null");
            }

            if(!isScript) {
                writeln(message);
            }

            output = scanner.nextLine();
            output = output.isEmpty() ? null : output;
        }while (!isScript && !canNull && output == null);
        if(isScript && output == null)
            throw new InvalidValueException("Ожидалось не null строка");

        return output;
    }

    /**
     * выводит сообщение с вводом от пользователя, с проверкой на минимальное и макскимальное значение
     * @param message
     * @param min
     * @param max
     * @param canNull
     * @return
     */
    public String readWithMessageMinMax(String message, BigDecimal min, BigDecimal max, boolean canNull){
        String output = "";

        do {
            output = readWithMessage(message, canNull);
        }while (!NumUtil.isInRange(Double.parseDouble(output), min, max));

        return output;
    }

    public String read() {
        return scanner.nextLine();
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * получает введенные данные объектом
     * @return
     */
    public City getCity(){
        String name = readWithMessage("Введите название города: ", false);
        Coordinates coord = getCoord();
        Double area = Double.parseDouble(readWithMessage("Введите площадь (Double): ", false));
        long population = Long.parseLong(readWithMessageMinMax("Введите кол-во населения (Long): ", new BigDecimal(0), NumUtil.LONG_MAX, true));
        long metersAboveSeaLevel = Long.parseLong(readWithMessage("Введите высоту над уровнем моря (Long): ", true));
        double timezone = Double.parseDouble(readWithMessageMinMax("Введите часовой пояс (Double): ", new BigDecimal(-13), new BigDecimal(15), true));
        boolean capital = parseBoolean(readWithMessage("Это столица? (true/false): ", true));
        Government government = getGoverment();
        Human human = getHuman();

        return new City(name, coord, area, population, metersAboveSeaLevel, timezone, capital, government, human);
    }


    /**
     * получает координаты
     * @return
     */
    public Coordinates getCoord(){
        Float x = Float.parseFloat(readWithMessage("Введите позицию X (Float): ", true));
        Double y = Double.parseDouble(readWithMessageMinMax("Введите позицию Y (Double): ", new BigDecimal(-587), NumUtil.DOUBLE_MAX, false));

        return new Coordinates(x, y);
    }

    /**
     * получает тип правления
     * @return
     */
    public Government getGoverment() {
        StringBuilder sb = new StringBuilder();
        for (Government value : Government.values()) {
            sb.append("\n").append(value.ordinal()).append(" - ").append(value.toString());
        }
        String inp = readWithMessage("Какой вид правления? Введите число или пустую строку: " + sb.toString(), true);
        if (inp == null) {
            return null;
        }
        return Government.byOrdinal(Integer.parseInt(inp));
    }


    /**
     * получает возраст человека
     * @return
     */
    public Human getHuman(){
        Integer age = Integer.parseInt(readWithMessageMinMax("Введите возвраст (Integer): ", new BigDecimal(0), NumUtil.INTEGER_MAX, true));
        return new Human(age);
    }

    /**
     * парсит boolean значение из сотроки
     * @param s
     * @return
     */
    public static boolean parseBoolean(String s) {
        if ("true".equals(s.toLowerCase())) {
            return true;
        } else if ("false".equals(s.toLowerCase())) {
            return false;
        }
        return false;
    }
}
