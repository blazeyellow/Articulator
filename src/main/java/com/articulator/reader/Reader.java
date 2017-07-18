package com.articulator.reader;

import java.io.FileNotFoundException;
import java.util.List;

public interface Reader {

    public List readAll(String filename, String[] columns) throws FileNotFoundException;

}
