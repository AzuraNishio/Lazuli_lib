package lazuli_lib.lazuli.data_containers;

import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.List;

public class LazuliLineBuffer {

    // List to store multiple lines
    private final List<LazuliLine> lines;

    // Constructor initializes an empty list
    public LazuliLineBuffer() {
        this.lines = new ArrayList<>();
    }

    // Adds a new line to the buffer
    public void addLine(Vec3d vertex1, Vec3d vertex2, double width, double z, int color) {
        lines.add(new LazuliLine(vertex1, vertex2, width, z, color));
    }

    public void addLine(LazuliLine Line) {
        lines.add(Line);
    }

    // Returns the list of all stored lines
    public List<LazuliLine> getLines() {
        return lines;
    }

    // Clears the buffer (removes all lines)
    public void clear() {
        lines.clear();
    }


}
