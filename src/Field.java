import java.awt.*;

public class Field {

    private static final int canvasSize = 400;

    private static Color[][] genLandscape(int size) {
        Color[][] array = new Color[size][size];
        int random = 0;
        Color felsen = Color.gray, wiese = Color.green;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                random = (int) (Math.random() * 100);
                if(random <= 10){
                    array[i][j] = felsen;
                } else {
                    array[i][j] = wiese;
                }
            }
        }
        return array;
    }

    private static void drawLandscape(Color[][] landscape) {
        for (int i = 0; i < landscape.length; i++) {
            for (int j = 0; j < landscape[i].length; j++) {
                StdDraw.setPenColor(landscape[i][j]);
                StdDraw.filledSquare(2 + (j * 4),2 + (i * 4),2);
            }
        }
    }

    private static void simLiquidFlow(Color[][] landscape, int x, int y) {
        int random = (int) (Math.random() * 100);
        if (y >= 0 && y < 100 && x >= 0 && x < 100) {
            if(y == 0){
                //Erstes Quadrat zur Sicherheit orange, denn bei nächsten Aufrufen, die "y - 1" beinhalten,
                //existiert bei y = 0 kein y = -1
                landscape[y][x] = Color.orange;
                simLiquidFlow(landscape, x, y + 1);
            } else {
                if (random <= 50) {
                    x--;
                    //Die Flüssigkeit fließt nach links
                    if (landscape[y][x] == Color.gray) {
                        //Fließt die Flüssigkeit von rechts unten auf einen grauen Felsen links oben...
                        landscape[y][x] = Color.black;
                        //Grauer Felsen wird schwarz
                        landscape[y - 1][x] = Color.orange;
                        //Unter dem Felsen wird die Wiese orange
                        landscape[y][x + 1] = Color.orange;
                        //Wiese rechts vom Felsen wird orange
                        simLiquidFlow(landscape, x + 1, y + 1);
                        simLiquidFlow(landscape, x, y);
                        //Das x wird miteingerechnet, weshalb um eine Zeile nach oben und beim nächsten
                        //Aufruf entweder links oder rechts, dass von x-- bzw. x++ abhängig ist
                    } else if(landscape[y][x] == Color.black) {
                        //"Trifft die Flüssigkeit auf einen schwarzen Felsen, dann passiert nichts,
                        // d.h. der Aufruf wird beendet."
                    } else {
                        landscape[y][x] = Color.orange;
                        simLiquidFlow(landscape, x, y + 1);
                    }
                } else {
                    //Analog nur umgekehrt zur oberen Situation
                    x++;
                    if (landscape[y][x] == Color.gray) {
                        landscape[y][x] = Color.black;
                        landscape[y - 1][x] = Color.orange;
                        landscape[y][x - 1] = Color.orange;
                        simLiquidFlow(landscape, x - 1, y + 1);
                        simLiquidFlow(landscape, x, y);
                    } else if(landscape[y][x] == Color.black) {
                    } else {
                        landscape[y][x] = Color.orange;
                        simLiquidFlow(landscape, x, y + 1);
                    }
                }
            }
        }
    }

    private static void simSpreadingFire(Color[][] landscape, int x, int y) {
        int random = (int) (Math.random() * 100);
        if(x >= 0 && x < 100 && y >= 0 && y < 100){
            if(landscape[y][x] == Color.green){
                landscape[y][x] = Color.red;
                if(random <= 60){
                    // North
                    simSpreadingFire(landscape, x, y + 1);
                    // East
                    simSpreadingFire(landscape, x + 1, y);
                    // South
                    simSpreadingFire(landscape, x, y - 1);
                    // West
                    simSpreadingFire(landscape, x - 1, y);
                }
            }
            if(landscape[y][x] == Color.gray){ }
            if(landscape[y][x] == Color.orange){
                spreadFireInLiquid(landscape, x, y);
            }
        }
    }

    private static void spreadFireInLiquid(Color[][] landscape, int x, int y) {
        if(x >= 0 && x < 100 && y >= 0 && y < 100){
            if(landscape[y][x] == Color.orange){
                landscape[y][x] = Color.red;
                // North
                spreadFireInLiquid(landscape, x, y + 1);
                // East
                spreadFireInLiquid(landscape, x + 1, y);
                // South
                spreadFireInLiquid(landscape, x, y - 1);
                // West
                spreadFireInLiquid(landscape, x - 1, y);
                // North-east
                spreadFireInLiquid(landscape, x + 1, y + 1);
                // North-west
                spreadFireInLiquid(landscape, x - 1, y + 1);
                // South-east
                spreadFireInLiquid(landscape, x + 1, y - 1);
                // South-west
                spreadFireInLiquid(landscape, x - 1, y - 1);
            }
        }
    }

    public static void main(String[] args) {
        StdDraw.setCanvasSize(canvasSize, canvasSize);
        StdDraw.setScale(0, canvasSize);
        StdDraw.enableDoubleBuffering();

        int size = 100;
        Color[][] landscape = genLandscape(size);

        simLiquidFlow(landscape, size / 2, 0);
        drawLandscape(landscape);
        StdDraw.show();
        StdDraw.pause(2000);

        landscape[75][25] = Color.GREEN;
        simSpreadingFire(landscape, 75, 25);
        drawLandscape(landscape);
        StdDraw.show();
        StdDraw.save("./src/result.jpg");
    }
}
