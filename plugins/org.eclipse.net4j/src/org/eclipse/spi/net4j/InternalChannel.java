/*
 * Copyright (c) 2008, 2009, 2011, 2012, 2019, 2020 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.spi.net4j;

import org.eclipse.net4j.buffer.IBuffer;
import org.eclipse.net4j.buffer.IBufferProvider;
import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.channel.IChannelMultiplexer;
import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.lifecycle.ILifecycle;

import java.util.Queue;
import java.util.concurrent.ExecutorService;

/**
 * If the meaning of this type isn't clear, there really should be more of a description here...
 *
 * @author Eike Stepper
 */
public interface InternalChannel extends IChannel, IBufferProvider, ILifecycle
{
  /**
   * @since 2.0
   */
  public void setID(short id);

  /**
   * @since 2.0
   */
  public void setUserID(String userID);

  /**
   * @deprecated As of 4.8 no longer supported.
   */
  @Deprecated
  public ExecutorService getReceiveExecutor();

  /**
   * @deprecated As of 4.8 no longer supported.
   */
  @Deprecated
  public void setReceiveExecutor(ExecutorService receiveExecutor);

  /**
   * @since 2.0
   */
  public void setMultiplexer(IChannelMultiplexer channelMultiplexer);

  public void handleBufferFromMultiplexer(IBuffer buffer);

  public Queue<IBuffer> getSendQueue();

  /**
   * An {@link IEvent event} fired from a {@link InternalChannel channel} when a {@link IBuffer buffer} is enqueued or
   * dequeued.
   *
   * @author Eike Stepper
   * @since 3.0
   * @noextend This interface is not intended to be extended by clients.
   * @noimplement This interface is not intended to be implemented by clients.
   */
  public interface SendQueueEvent extends IEvent
  {
    @Override
    public InternalChannel getSource();

    public Type getType();

    public int getQueueSize();

    /**
     * Enumerates the possible {@link InternalChannel#getSendQueue() send queue} {@link SendQueueEvent event} types.
     *
     * @author Eike Stepper
     */
    public enum Type
    {
      ENQUEUED, DEQUEUED
    }
  }

  /**
   * An {@link IEvent event} fired from a {@link InternalChannel channel} when one of its statistical counters changed.
   *
   * @see IChannel#getReceivedBuffers()
   * @see IChannel#getReceivedBytes()
   * @see IChannel#getSentBuffers()
   * @see IChannel#getSentBytes()
   * @author Eike Stepper
   * @since 4.13
   * @noextend This interface is not intended to be extended by clients.
   * @noimplement This interface is not intended to be implemented by clients.
   */
  public interface CountersChangedEvent extends IEvent
  {
    @Override
    public InternalChannel getSource();
  }
}
