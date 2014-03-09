package com.appacitive.core.query;

import com.appacitive.core.infra.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley.
 */
public class GeoFilter extends Filter {

    public GeoFilter(String propertyName) {
        this.key = propertyName;
    }

    public GeoFilter withinCircle(double[] centre, double radius, DistanceMetric metric) {
        this.operator = "within_circle";
        this.value = String.format("%s,%s,%s %s", centre[0], centre[1], radius, metric.name());
        return this;
    }

    public GeoFilter withinPolygon(List<double[]> points) {
        this.operator = "within_polygon";
        List<String> strPoints = new ArrayList<String>();
        for (double[] point : points) {
            strPoints.add(String.format("%s,%s", String.valueOf(point[0]), String.valueOf(point[1])));
        }
        this.value = StringUtils.join(strPoints, "|");
        return this;
    }

    @Override
    public String asString() {
        return String.format("*%s %s %s", this.key, this.operator, this.value);
    }
}
