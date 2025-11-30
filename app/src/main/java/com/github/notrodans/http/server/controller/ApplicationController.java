package com.github.notrodans.http.server.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import com.github.notrodans.http.server.bind.RequestMapping;
import com.github.notrodans.http.server.common.ApplicationParameters;
import com.github.notrodans.http.server.common.HttpHeaders;
import com.github.notrodans.http.server.common.HttpMethod;
import com.github.notrodans.http.server.common.HttpStatus;
import com.github.notrodans.http.server.request.RequestContext;
import com.github.notrodans.http.server.response.ResponseContext;

public class ApplicationController {
	@RequestMapping(path = "/", method = HttpMethod.GET)
	public ResponseContext simpleOk(final RequestContext context) {
		return ResponseContext.build(HttpStatus.OK);
	}

	@RequestMapping(path = "/echo/{command}", method = HttpMethod.GET)
	public ResponseContext echo(final RequestContext context) {
		final var responseBody = context.getLastPart();
		return ResponseContext
			.build(
				HttpStatus.OK,
				HttpHeaders
					.fromHeaderMap(
						Map
							.of(
								"Content-Type",
								"text/plain",
								"Content-Length",
								String.valueOf(responseBody.getBytes().length))),
				responseBody);
	}

	@RequestMapping(path = "/user-agent", method = HttpMethod.GET)
	public ResponseContext userAgent(final RequestContext context) {
		final var responseBody = context.getHeaders().getFirst("User-Agent");
		return ResponseContext
			.build(
				HttpStatus.OK,
				HttpHeaders
					.fromHeaderMap(
						Map
							.of(
								"Content-Type",
								"text/plain",
								"Content-Length",
								String.valueOf(responseBody.getBytes().length))),
				responseBody);
	}

	@RequestMapping(path = "/files/{file}", method = HttpMethod.POST)
	public ResponseContext saveFile(final RequestContext context) {
		if (!ApplicationParameters.getInstance().isDirectoryExists()) {
			return ResponseContext.build(HttpStatus.NOT_FOUND);
		}
		var directory = ApplicationParameters.getInstance().getFileDirectory();
		directory = directory.endsWith("/") ? directory : directory + "/";
		final var fileName = context.getLastPart();
		saveFile(String.format("%s%s", directory, fileName), context.getBody());
		return ResponseContext.build(HttpStatus.CREATED);
	}

	@RequestMapping(path = "/files/{file}", method = HttpMethod.GET)
	public ResponseContext readFileByName(final RequestContext context) {
		if (!ApplicationParameters.getInstance().isDirectoryExists()) {
			return ResponseContext.build(HttpStatus.NOT_FOUND);
		}

		var directory = ApplicationParameters.getInstance().getFileDirectory();
		directory = directory.endsWith("/") ? directory : directory + "/";
		final var fileName = context.getLastPart();
		final var file = new File(String.format("%s%s", directory, fileName));
		if (!file.exists() || !file.isFile()) {
			return ResponseContext.build(HttpStatus.NOT_FOUND);
		}

		final var fileContent = readFile(file);
		return ResponseContext
			.build(
				HttpStatus.OK,
				HttpHeaders
					.fromHeaderMap(
						Map
							.of(
								"Content-Type",
								"application/octet-stream",
								"Content-Length",
								String.valueOf(fileContent.getBytes().length))),
				fileContent);
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
