package managers;

import collection.City;
import collection.Coordinates;
import collection.Government;
import collection.Human;
import exceptions.ExecutionException;
import exceptions.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import utils.NumUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Scanner;

/**
 * Консольный менеджер
 */
@Slf4j
public class ConsoleManager {

    private Scanner scanner;
    private boolean isScript;
    private Writer writer;
    private Reader reader;

    public ConsoleManager(Reader reader, Writer writer, boolean isScript)
    {
        this.reader = reader;
        this.writer = writer;
        scanner = new Scanner(reader);
        this.isScript = isScript;
    }

    public void writeln(String message) {
        write(message + "\n");
    }

    public void write(String message) {
        try {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            log.error("Ошибка при выводе. {}", e.getMessage());
        }
    }

    public boolean getIsScript(){ return isScript; }

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
                writeln("Вы ввели пустую строку, попробуйте снова");
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

    public Number readWithParse(String msg, boolean canNull){
        Number out = null;
        while (true){
            try{
                String num = readWithMessage(msg, canNull);
                NumberFormat format = NumberFormat.getInstance();
                ParsePosition pos = new ParsePosition(0);
                out = format.parse(num, pos);
                if (pos.getIndex() != num.length() || pos.getErrorIndex() != -1) {
                    throw new NumberFormatException("Неверный тип данных");
                }
                break;
            }catch (NumberFormatException ex){
                writeln(ex.getMessage());
            }
        }

        return out;
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

        //Long q = (long)readWithParse("Test value", false);
        //writeln(q.toString());

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
                if(isScript) throw new ExecutionException("Cast error");
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
                if(isScript) throw new ExecutionException("Cast error");
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
                if(isScript) throw new ExecutionException("Cast error");
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
                if(isScript) throw new ExecutionException("Cast error");
            }
        }

        while (true) {
            try {
                capital = parseBoolean(readWithMessage("Это столица? (true/false): ", false));
                break;
            }catch(NumberFormatException ex){
                writeln("Так true или false?");
                if(isScript) throw new ExecutionException("Cast error");
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
                if(isScript) throw new ExecutionException("");
            }
        }

        while (true) {
            try {
                String o = readWithMessageMinMax("Введите позицию Y (Double, от -587 до max): ", new BigDecimal(-587), NumUtil.DOUBLE_MAX, false);
                if(o == null)
                    break;
                y = Double.parseDouble(o);
                break;
            } catch (NumberFormatException ex) {
                writeln("Неверный тип данных");
                if(isScript) throw new ExecutionException("Cast error");
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
                if(isScript) throw new ExecutionException("Cast error");
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
                if(isScript) throw new ExecutionException("Cast error");
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
