/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.gateway.filter.factory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.server.ServerWebExchange;

/**
 * This filter changes the request uri by a request header.
 *
 * @author Toshiaki Maki
 */
public class RequestHeaderToRequestUriGatewayFilterFactory extends
		AbstractChangeRequestUriGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {

	private final Logger log = LoggerFactory
			.getLogger(RequestHeaderToRequestUriGatewayFilterFactory.class);

	public RequestHeaderToRequestUriGatewayFilterFactory() {
		super(NameConfig.class);
	}

	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList(NAME_KEY);
	}

	@Override
	protected Optional<URI> determineRequestUri(ServerWebExchange exchange,
			NameConfig config) {
		String requestUrl = exchange.getRequest().getHeaders().getFirst(config.getName());
		return Optional.ofNullable(requestUrl).map(url -> {
			try {
				return new URL(url).toURI();
			}
			catch (MalformedURLException | URISyntaxException e) {
				log.info("Request url is invalid : url={}, error={}", requestUrl,
						e.getMessage());
				return null;
			}
		});
	}

}