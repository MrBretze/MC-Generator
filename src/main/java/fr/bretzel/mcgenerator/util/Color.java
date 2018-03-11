package fr.bretzel.mcgenerator.util;

import java.util.HashMap;

public enum Color
{
    WHITE(0, 255, 255, 255, "White"),
    ORANGE(1, 240, 118, 19, "Orange"),
    MAGENTA(2, 189, 68, 179, "Magenta"),
    LIGHT_BLUE(3, 58, 175, 217, "Light Blue"),
    YELLOW(4, 248, 198, 39, "Yellow"),
    LIME(5, 112, 185, 25, "Lime"),
    PINK(6, 237, 141, 172, "Pink"),
    GRAY(7, 62, 68, 71, "Gray"),
    LIGHT_GRAY(8, 142, 142, 134, "Light Gray"),
    CYAN(9, 21, 137, 145, "Cyan"),
    PURPLE(10, 121, 42, 172, "Purple"),
    BLUE(11, 53, 57, 157, "Blue"),
    BROWN(12, 114, 71, 40, "Brown"),
    GREEN(13, 84, 109, 27, "Green"),
    RED(14, 161, 39, 34, "Red"),
    BLACK(15, 20, 21, 25, "Black");


    private int r, g, b, id;
    private String display_name;
    private static HashMap<String, Color> display_map = new HashMap<>();

    static
    {
        for (Color color : values())
        {
            display_map.put(color.getDisplayName(), color);
        }
    }

    Color(int id, int r, int g, int b, String display_name)
    {
        this.display_name = display_name;
        this.id = id;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getB()
    {
        return b;
    }

    public int getG()
    {
        return g;
    }

    public int getR()
    {
        return r;
    }

    public int getId()
    {
        return id;
    }

    public String getDisplayName()
    {
        return display_name;
    }

    public static Color getColorByDisplayName(String display_name)
    {
        return display_map.get(display_name);
    }
}
