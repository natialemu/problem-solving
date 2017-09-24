class LineSegmentVector {
    Point startPoint;
    Point endPoint;
    double magnitude;
    LineSegmentVector unitVector;
    LineSegmentVector difference;

    public LineSegmentVector(double x1, double x2 , double y1, double y2){
        startPoint = new Point();
        startPoint.x = x1;
        startPoint.y  = y1;
        endPoint = new Point();
        endPoint.x = x2;
        endPoint.y = y2;
        magnitude = Math.abs(startPoint.getDistance(endPoint));
    }

    public void setUnitVector(double x1, double x2, double y1, double y2) {
        unitVector = new LineSegmentVector(x1, x2, y1, y2);
    }

    @Override
    public String toString(){
        double xdiff = endPoint.x - startPoint.x;
        double ydiff = endPoint.y - startPoint.y;

        return "[ " + xdiff + "i + " + ydiff+"j ]";
    }

    public List<Point> getMarkers(){

        LineSegmentVector p = new LineSegmentVector(0,0,1,1);
        cloneVector(p);

        List<Point> markers = new ArrayList<>();
        p.setUnitVector(p.startPoint.x,p.startPoint.x + ((p.endPoint.x - p.startPoint.x)/p.magnitude),p.startPoint.y,p.startPoint.y + ((p.endPoint.y - p.startPoint.y)/p.magnitude));

        LineSegmentVector pUnit = p.unitVector;
        if(difference.isNotZero()){
            LineSegmentVector mockUnit = new LineSegmentVector(0,0,1,1);
            pUnit.cloneVector(mockUnit);
            mockUnit.scalarProduct(difference.magnitude);

            pUnit.shiftBack(mockUnit);
            Point pt = new Point();
            pt.x = pUnit.endPoint.x;
            pt.y = pUnit.endPoint.y;
            markers.add(pt);
        }else{
            markers.add(pUnit.startPoint);
            Point pt = new Point();
            pt.x = pUnit.endPoint.x;
            pt.y = pUnit.endPoint.y;
            markers.add(pt);
        }

        while((Math.abs(pUnit.magnitude - (magnitude+difference.magnitude)) > unitVector.magnitude)) {

            pUnit.increaseVectorBy(unitVector);
            Point pt1 = new Point();
            pt1.x = pUnit.endPoint.x;
            pt1.y = pUnit.endPoint.y;
            markers.add(pt1);

        }


        double finalDifference = Math.abs(pUnit.magnitude - (magnitude  ));

        if(finalDifference >= 0.5){
            markers.add(endPoint);
        }else{
            double diffX1 = pUnit.endPoint.x;
            double diffX2 = endPoint.x;
            double diffY1 = pUnit.endPoint.y;
            double diffY2 = endPoint.y;

            difference = new LineSegmentVector(diffX1,diffX2,diffY1,diffY2);
        }
        return markers;


    }

    private void scalarProduct(double magnitude) {


        startPoint.x  = magnitude*startPoint.x;
        startPoint.y = magnitude*startPoint.y;
        endPoint.x = magnitude*endPoint.x;
        endPoint.y = magnitude*endPoint.y;



    }

    private void shiftBack(LineSegmentVector difference) {

        startPoint.x = startPoint.x - (difference.endPoint.x - difference.startPoint.x);
        startPoint.y = startPoint.y - (difference.endPoint.y - difference.startPoint.y);

        endPoint.x = endPoint.x - (difference.endPoint.x - difference.startPoint.x);
        endPoint.y = endPoint.y - (difference.endPoint.y - difference.startPoint.y);
    }

    public void increaseVectorBy(LineSegmentVector p) {
        endPoint.x = endPoint.x + (p.endPoint.x - p.startPoint.x);
        endPoint.y = endPoint.y + (p.endPoint.y - p.startPoint.y);
        magnitude = startPoint.getDistance(endPoint);

    }

    public void cloneVector(LineSegmentVector p) {
        p.startPoint.x = startPoint.x;
        p.startPoint.y = startPoint.y;
        p.endPoint.x = endPoint.x;
        p.endPoint.y = endPoint.y;
        p.magnitude = magnitude;
        unitVector = new LineSegmentVector((p.startPoint.x)/p.magnitude,(p.endPoint.x)/p.magnitude, (p.startPoint.y)/p.magnitude, (p.endPoint.y)/p.magnitude);
    }

    public boolean isNotZero() {
        if(startPoint.x != 0 && startPoint.y != 0 && endPoint.x != 0 && endPoint.y != 0) return true;
        return false;
    }

    public void setDifference(LineSegmentVector difference) {

        this.difference = new LineSegmentVector(difference.startPoint.x,difference.endPoint.x,difference.startPoint.y,difference.endPoint.y);
    }

    public void setDifference(double x1, double x2, double y1, double y2){
        difference = new LineSegmentVector(x1,x2,y1,y2);

    }
}

class Point{
    double x;
    double y;



    public double getDistance(Point p){
        double d = Math.sqrt((p.x - x)*(p.x - x) + (p.y-y)*(p.y - y));
        return d;

    }

    @Override
    public String toString(){
        return "("+x+", " + y+ ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (point.x != x) return false;
        return point.y == y;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

class Tower{
    Point x;
    double power;
    char label;

}


public class cell{

	public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String s = scan.nextLine();

        while(!s.trim().equals("0") || !s.trim().equals("")){

            String[] firstLine = s.split(" ");

            List<Tower> towers = new ArrayList<>();
            for (int i = 0; i < Integer.parseInt(firstLine[0]); i++) {
                String[] line = scan.nextLine().split(" ");
                Tower t = new Tower();
                Point p = new Point();
                p.x = Integer.parseInt(line[0]);
                p.y = Integer.parseInt(line[1]);

                t.x = p;
                t.power = Integer.parseInt(line[2]);
                t.label = ((char) ('A' + i));
                towers.add(t);
            }
            String[] segments = scan.nextLine().split(" ");

            List<LineSegmentVector> lineSegmentVectors = new ArrayList<>();
            boolean first = true;
            for (int i = 0; i < segments.length; i = i + 4) {
                if (!first) {
                    i = i - 2;
                }
                first = false;

                double x1 = Integer.parseInt(segments[i]);
                double y1 = Integer.parseInt(segments[i + 1]);

                double x2 = Integer.parseInt(segments[i + 2]);
                double y2 = Integer.parseInt(segments[i + 3]);

                LineSegmentVector ls = new LineSegmentVector(x1, x2, y1, y2);
                double magnitude = ls.magnitude;
                ls.setUnitVector(ls.startPoint.x, ls.startPoint.x + ((ls.endPoint.x - ls.startPoint.x) / magnitude), ls.startPoint.y, ls.startPoint.y + ((ls.endPoint.y - ls.startPoint.y) / magnitude));
                ls.setDifference(0,0,0,0);

                lineSegmentVectors.add(ls);

            }

            List<Point> allMarkers = new ArrayList<>();

            Set<Point> markersSet = new HashSet<>();

            for (int i = 0; i < lineSegmentVectors.size(); i++) {

                List<Point> markers = lineSegmentVectors.get(i).getMarkers();
                if (lineSegmentVectors.get(i).difference.isNotZero() && (i + 1) < lineSegmentVectors.size()) {

                    lineSegmentVectors.get(i + 1).setDifference(lineSegmentVectors.get(i).difference);
                }
                for (Point pt : markers) {
                    if (!markersSet.contains(pt)) {
                        allMarkers.add(pt);
                        markersSet.add(pt);
                    }
                }
            }

            Tower currentMaxTower = null;
            for (int i = 0; i < allMarkers.size(); i++) {
                double maxdistance = Double.NEGATIVE_INFINITY;
                Tower maxTower = null;
                for (Tower t : towers) {
                    double strength = Math.ceil(t.power / (allMarkers.get(i).getDistance(t.x) * allMarkers.get(i).getDistance(t.x)));
                    if (strength > maxdistance) {
                        maxdistance = strength;
                        maxTower = t;
                    }
                }
                if (currentMaxTower == null || maxTower.label != currentMaxTower.label) {
                    currentMaxTower = maxTower;
                    System.out.print("(" + i + "," + maxTower.label + ")" + " ");
                }
            }
            System.out.println();
            s = scan.nextLine();
        }

    }





}