/* Copyright 2009-2018 EPFL, Lausanne */

package stainless
package extraction
package utils

import java.util.concurrent.ConcurrentHashMap

class ConcurrentCache[A,B](underlying: ConcurrentHashMap[A,B] = new ConcurrentHashMap[A,B]) {
  def get(key: A): Option[B] = Option(underlying.get(key))
  def update(key: A, value: B): Unit = underlying.put(key, value)
  def contains(key: A): Boolean = underlying.containsKey(key)
  def apply(key: A): B = get(key).get

  def cached(key: A)(value: => B): B = {
    val result = underlying.get(key)
    if (result != null) result
    else underlying.putIfAbsent(key, value)
  }

  def retain(p: A => Boolean): Unit = synchronized {
    val it = underlying.keySet.iterator
    while (it.hasNext) {
      if (!p(it.next)) it.remove
    }
  }
}

class ConcurrentCached[A,B](builder: A => B) extends (A => B) {
  private[this] val cache: ConcurrentCache[A,B] = new ConcurrentCache[A,B]
  override def apply(key: A): B = cache.cached(key)(builder(key))
}