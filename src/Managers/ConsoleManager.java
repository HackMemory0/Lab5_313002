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
            if(output == null && canNull)
                break;
        }while (!NumUtil.isInRange2(new BigDecimal(output), min, max));

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
        Double area = 0.0, timezone = 0.0;
        long population = 0, metersAboveSeaLevel = 0;
        boolean capital = false;

        String name = readWithMessage("Введите название города: ", false);
        Coordinates coord = getCoord();

        while (true) {
            try {
                String o = readWithMessageMinMax("Введите площадь (Double, от нуля и больше): ", new BigDecimal(0), NumUtil.DOUBLE_MAX, false);
                if (o == null)
                    break;
                area = Double.parseDouble(o);
                break;
            } catch (NumberFormatException ex) {
                writeln("Неверный тип данных");
            }
        }

        while (true) {
            try {
                String o = readWithMessageMinMax("Введите кол-во населения (Long, Больше нуля): ", new BigDecimal(0), NumUtil.LONG_MAX, true);
                if(o == null)
                    break;
                population = Long.parseLong(o);
                break;
            }catch(NumberFormatException ex){
                writeln("Неверный тип данных");
            }
        }

        while (true) {
            try {
                String o = readWithMessage("Введите высоту над уровнем моря (Long): ", true);
                if(o == null)
                    break;
                metersAboveSeaLevel = Long.parseLong(o);
                break;
            }catch(NumberFormatException ex){
                writeln("Неверный тип данных");
            }
        }

        while (true) {
            try {
                String o = readWithMessageMinMax("Введите часовой пояс (Double, от -13 до 15): ", new BigDecimal(-13), new BigDecimal(15), true);
                if(o == null)
                    break;
                timezone = Double.parseDouble(o);
                break;
            }catch(NumberFormatException ex){
                writeln("Неверный тип данных");
            }
        }

        while (true) {
            try {
                capital = parseBoolean(readWithMessage("Это столица? (true/false): ", false));
                break;
            }catch(NumberFormatException ex){
                writeln("Так true или false?");
            }
        }


        Government government = getGoverment();
        Human human = getHuman();

        return new City(name, coord, area, population, metersAboveSeaLevel, timezone, capital, government, human);
    }


    /**
     * получает координаты
     * @return
     */
    public Coordinates getCoord(){
        Float x = 0f;
        Double y = 0.0;

        while (true) {
            try {
                String o = readWithMessage("Введите позицию X (Float): ", true);
                if(o == null)
                    break;

                x = Float.parseFloat(o);
                break;
            } catch (NumberFormatException ex) {
                writeln("Неверный тип данных");
            }
        }

        while (true) {
            try {
                String o = readWithMessageMinMax("Введите позицию Y (Double, от -587 до max): ", new BigDecimal(-587.00000000000000000001), NumUtil.DOUBLE_MAX, false);
                if(o == null)
                    break;
                y = Double.parseDouble(o);
                break;
            } catch (NumberFormatException ex) {
                writeln("Неверный тип данных");
            }
        }

        return new Coordinates(x, y);
    }

    /**
     * получает тип правления
     * @return
     */
    public Government getGoverment() {
        Government out = null;
        StringBuilder sb = new StringBuilder();
        for (Government value : Government.values()) {
            sb.append("\n").append(value.ordinal()).append(" - ").append(value.toString());
        }

        while (true) {
            try {
                String inp = readWithMessage("Какой вид правления? Введите число: " + sb.toString(), false);
                if (inp == null) {
                    return null;
                }
                out = Government.byOrdinal(Integer.parseInt(inp));
                break;
            } catch (ClassCastException | InvalidValueException ex) {
                writeln(ex.getMessage());
            }
        }

        return out;
    }


    /**
     * получает возраст человека
     * @return
     */
    public Human getHuman(){

        Integer age = 0;

        while (true) {
            try {
                String o = readWithMessageMinMax("Введите возвраст (Integer, от 0 до INTEGER_MAX): ", new BigDecimal(0), NumUtil.INTEGER_MAX, true);
                if(o == null)
                    break;
                age = Integer.parseInt(o);
                break;
            } catch (NumberFormatException ex) {
                writeln("Неверный тип данных");
            }
        }

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
        }else{
            throw new NumberFormatException("Неверный тип данных");
        }
    }
}
