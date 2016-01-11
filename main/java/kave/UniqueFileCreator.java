/**
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *    - Sebastian Proksch
 */
package kave;

import java.io.File;
import java.io.IOException;

import com.google.inject.Inject;

public class UniqueFileCreator {

    private File root;
    private String extension;
    private int numberOfFilesInRoot;

    @Inject
    public UniqueFileCreator(File root, String extension) {
        if (extension.startsWith(".")) {
            throw new KaVEException("extension must not start with a '.'");
        }
        this.extension = extension;
        if (!root.exists() || !root.isDirectory()) {
            throw new KaVEException("root folder does not exist");
        }
        this.root = root;
        this.numberOfFilesInRoot = root.list().length;
    }

    public synchronized File createNextUniqueFile() throws IOException {
        File nextFile = getNextFile();
        while (nextFile.exists()) {
            nextFile = getNextFile();
            //add model
        }
        return nextFile;
    }

    private File getNextFile() {
        String nextFileName = (numberOfFilesInRoot++) + "." + extension;
        return new File(root, nextFileName);
    }
}