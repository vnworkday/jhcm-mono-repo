package io.github.ntduycs.jhcm.base.http.handler;

import io.github.ntduycs.jhcm.base.http.HttpProperties;
import io.github.ntduycs.jhcm.base.http.interceptor.ClientRequestLoggingInterceptor;
import io.github.ntduycs.jhcm.base.http.interceptor.ClientResponseLoggingInterceptor;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientLoggingHandler extends ChannelDuplexHandler {
  private final ClientRequestLoggingInterceptor requestLogger =
      new ClientRequestLoggingInterceptor();
  private final ClientResponseLoggingInterceptor responseLogger =
      new ClientResponseLoggingInterceptor();

  private final HttpProperties properties;

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
      throws Exception {
    switch (msg.getClass().getSimpleName()) {
      case "FullHttpRequest" -> requestLogger.logRequest((FullHttpRequest) msg);
      case "HttpRequest" -> requestLogger.logRequest((HttpRequest) msg);
      case "FullHttpMessage" -> requestLogger.logRequest((FullHttpMessage) msg);
      default -> { // Do nothing
      }
    }

    super.write(ctx, msg, promise);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    switch (msg.getClass().getSimpleName()) {
      case "FullHttpResponse" -> responseLogger.logResponse((FullHttpResponse) msg);
      case "HttpResponse" -> responseLogger.logResponse((HttpResponse) msg);
      case "HttpContent" -> responseLogger.logResponse((HttpContent) msg);
      default -> { // Do nothing
      }
    }

    super.channelRead(ctx, msg);
  }
}
