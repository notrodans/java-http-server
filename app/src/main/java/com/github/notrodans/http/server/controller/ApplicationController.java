package com.github.notrodans.http.server.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import com.github.notrodans.http.server.bind.RequestMapping;
import com.github.notrodans.http.server.common.ApplicationParameters;
import com.github.notrodans.http.server.common.FromHeaderList;
import com.github.notrodans.http.server.common.FromHeaderMap;
import com.github.notrodans.http.server.common.HttpMethod;
import com.github.notrodans.http.server.common.HttpStatus;
import com.github.notrodans.http.server.request.RequestContext;
import com.github.notrodans.http.server.response.ResponseContext;
import com.github.notrodans.http.server.response.ResponseFromBytes;
import com.github.notrodans.http.server.response.ResponseFromString;

final public class ApplicationController {
	private final ApplicationParameters applicationParameters;

	public ApplicationController(final ApplicationParameters applicationParameters) {
		this.applicationParameters = applicationParameters;
	}

	@RequestMapping(path = "/", method = HttpMethod.GET)
	public ResponseContext simpleOk(final RequestContext context) {
		return new ResponseFromString(HttpStatus.OK, null, null).get();
	}

	@RequestMapping(path = "/echo/{command}", method = HttpMethod.GET)
	public ResponseContext echo(final RequestContext context) {
		final var responseBody = context.getLastPart();
		return new ResponseFromBytes(
			HttpStatus.OK,
			new FromHeaderList(
				List.of(
					"Content-Type",
					"text/plain",
					"Content-Length",
					String.valueOf(responseBody.getBytes().length)
				)
			).get(),
			responseBody.getBytes()
		).get();
	}

	@RequestMapping(path = "/user-agent", method = HttpMethod.GET)
	public ResponseContext userAgent(final RequestContext context) {
		final var responseBody = context.getHeaders().getFirst("User-Agent");
		return new ResponseFromBytes(
			HttpStatus.OK,
			new FromHeaderMap(
				Map.of(
					"Content-Type",
					"text/plain",
					"Content-Length",
					String.valueOf(responseBody.getBytes().length)
				)
			).get(),
			responseBody.getBytes()
		).get();
	}

	@RequestMapping(path = "/files/{file}", method = HttpMethod.POST)
	public ResponseContext saveFile(final RequestContext context) {
		if (!applicationParameters.isDirectoryExists()) {
			return new ResponseFromString(HttpStatus.NOT_FOUND, null, null).get();
		}
		var directory = applicationParameters.getFileDirectory();
		directory = directory.endsWith("/") ? directory : directory + "/";
		final var fileName = context.getLastPart();
		saveFile(String.format("%s%s", directory, fileName), context.getBody());
		return new ResponseFromString(HttpStatus.CREATED, null, null).get();
	}

	@RequestMapping(path = "/files/{file}", method = HttpMethod.GET)
	public ResponseContext readFileByName(final RequestContext context) {
		if (!applicationParameters.isDirectoryExists()) {
			return new ResponseFromString(HttpStatus.NOT_FOUND, null, null).get();
		}

		var directory = applicationParameters.getFileDirectory();
		directory = directory.endsWith("/") ? directory : directory + "/";
		final var fileName = context.getLastPart();
		final var file = new File(String.format("%s%s", directory, fileName));
		if (!file.exists() || !file.isFile()) {
			return new ResponseFromString(HttpStatus.NOT_FOUND, null, null).get();
		}

		final var fileContent = readFile(file);
		return new ResponseFromBytes(
			HttpStatus.OK,
			new FromHeaderMap(
				Map.of(
					"Content-Type",
					"application/octet-stream",
					"Content-Length",
					String.valueOf(fileContent.getBytes().length)
				)
			).get(),
			fileContent.getBytes()
		).get();
	}

	private void saveFile(final String fullPath, final String content) {
		try {
			final Path path = Paths.get(fullPath);
			if (Files.exists(path)) {
				Files.delete(path);
			}
			Files.write(path, content.getBytes());
		} catch (final Exception e) {
			throw new RuntimeException("Exception trying to save file", e);
		}
	}

	private String readFile(final File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			final byte[] data = new byte[(int) file.length()];
			fis.read(data);
			return new String(data);
		} catch (final IOException e) {
			throw new RuntimeException("Exception trying to read file", e);
		}
	}
}
