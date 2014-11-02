package com.crawler.autowereld.export.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

	private File file;

	public Serializer(String path) {
		this.setFile(new File(path + "_tmp.dat"));
	}

	public Serializer() {
	}

	public boolean existSerializedFile() {
		return file.exists();
	}

	public void save(DealersContainer data) throws SerializationException {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
		} catch (FileNotFoundException e) {
			throw new SerializationException("Wystapił błąd poczas zapisu do pliku tymczasowego.\nProgram nie moze znalezc pliku: " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new SerializationException("Wystapił błąd poczas zapisu do pliku tymczasowego.\nProgram nie moze zapisać danych do pliku: " + file.getAbsolutePath(), e);
		}
	}

	public DealersContainer read() throws SerializationException {
		DealersContainer data = new DealersContainer();
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			data = (DealersContainer) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			throw new SerializationException("Wystapił błąd poczas odczytu z pliku tymczasowego.\nProgram nie moze znalezc pliku: " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new SerializationException("Wystapił błąd poczas odczytu z pliku tymczasowego.\nProgram nie moze zapisać danych do pliku: " + file.getAbsolutePath(), e);
		} catch (ClassNotFoundException e) {
			throw new SerializationException("Wystapił błąd poczas odczytu z pliku tymczasowego.\nProgram nie moze zapisać danych do pliku: " + file.getAbsolutePath() + "\nSprawdź logi", e);
		}
		return data;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
