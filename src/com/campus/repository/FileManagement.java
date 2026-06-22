package com.campus.repository;

import java.io.*;

public class FileManagement {

    public static void saveToFile(String fileName, Object data) {

        File originalFile = new File(fileName);
        File backupFile = new File(fileName + ".bak");
        File parentDirectory = originalFile.getParentFile();

        if (parentDirectory != null && !parentDirectory.exists()){
            parentDirectory.mkdirs();
        }

        if (originalFile.exists()) {
            if (backupFile.exists()) {
                backupFile.delete();
            }

            originalFile.renameTo(backupFile);
            System.out.printf("Backup of %s created successfully.%n", fileName);
        }

        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(data);
            System.out.println("Data saved successfully.");

            out.close();
            fos.close();

        } catch (IOException ioe) {
            System.out.println("Error while saving data to file.");
            System.out.println("Restoring backup file...");

            if (backupFile.exists()) {
                backupFile.renameTo(originalFile);
                System.out.printf("Backup file %s restored successfully.%n", fileName);
            }
        } catch (Exception e) {
            System.out.println("Some exception occurred.");
            e.printStackTrace();
        }
    }

    public static Object loadIfBackupExists(String fileName) {
        File backupFile = new File(fileName + ".bak");
        if (backupFile.exists()) {
            return loadFromFile(fileName + ".bak");
        } else {
            System.out.println("No backup file found.");
            return null;
        }
    }

    public static Object loadFromFile(String fileName) {
        Object data = null;

        File fileToLoad = new File(fileName);

        if (!fileToLoad.exists()) {
            System.out.printf("File %s does not exist. Checking if backup exists.%n", fileName);

            return loadIfBackupExists(fileName);
        }

        if (fileToLoad.length() == 0) {
            System.out.printf("File %s is empty. Checking if backup exists.%n", fileName);

            return loadIfBackupExists(fileName);
        }

        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream input = new ObjectInputStream(fis);

            data = input.readObject();

            input.close();
            fis.close();
            return data;
        } catch (FileNotFoundException fn) {
            System.out.println("File not found.");
            return loadIfBackupExists(fileName);
        } catch (EOFException eof) {
            System.out.println("End of file is reached.");
            return loadIfBackupExists(fileName);
        } catch (StreamCorruptedException sce) {
            System.out.println("File is corrupted.");
            return loadIfBackupExists(fileName);
        } catch (IOException ioe) {
            System.out.println("Error while loading data from file.");
            return loadIfBackupExists(fileName);
        } catch (ClassNotFoundException cnf) {
            System.out.println("Data format mismatch. Class not found: " + cnf.getMessage());
        } catch (Exception e) {
            System.out.println("Some exception occurred.");
            e.printStackTrace();
        }

        return null;
    }
}
