
I have implemented this from a course : https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/blocking-queue--bounded-buffer--consumer-producer
---

```markdown
# Custom BlockingQueue in Java (with Lock and Condition)

This is a custom implementation of a **bounded blocking queue** in Java using low-level concurrency primitives from `java.util.concurrent.locks`.

It supports:

- Thread-safe **enqueue** and **dequeue**
- **Blocking** behavior on full/empty conditions
- A circular buffer for efficient space usage

---

## 🔧 How It Works

- Internally, the queue is backed by a fixed-size `Integer[]` array.
- `ReentrantLock` is used to ensure mutual exclusion between threads.
- A single `Condition` is used for both producers and consumers to wait/notify each other.
- Circular behavior is handled by resetting `head` and `tail` to `0` when they hit the array capacity.

---

## 📦 Code Overview

```java
Lock lock = new ReentrantLock();
Condition condition = lock.newCondition();
```

### `enqueue(int item)`
- Blocks when the queue is **full**.
- Wakes up all waiting threads using `signalAll()` after enqueuing.

### `dequeue()`
- Blocks when the queue is **empty**.
- Wakes up all waiting threads using `signalAll()` after dequeuing.

### Circular Index Reset
```java
if (tail == capacity) tail = 0;
if (head == capacity) head = 0;
```

---

## ❓ Interview Questions & Answers

### 1. What is the benefit of using `Lock` over `synchronized`?

| Feature | `synchronized` | `Lock` |
|--------|----------------|--------|
| Try Lock | ❌ | ✅ `tryLock()` |
| Interruptible Lock | ❌ | ✅ `lockInterruptibly()` |
| Multiple Conditions | ❌ | ✅ `lock.newCondition()` |
| Manual Lock/Unlock | ❌ | ✅ Fine-grained control |
| Fairness | ❌ | ✅ Fair locks supported |

Use `Lock` when you need **more flexibility**, **timeouts**, or **fine-grained condition handling**.

---

### 2. Why use `signalAll()` instead of `signal()`?

#### ✅ `signal()` wakes **one** waiting thread.
#### ✅ `signalAll()` wakes **all** waiting threads.

In this implementation, a **single `Condition`** is used for both **producers** (enqueue) and **consumers** (dequeue). If you use `signal()`:

- It may wake the wrong type of thread.
    - E.g., wake a producer when queue is full → thread immediately blocks again.
- This leads to **wasted CPU cycles**, **unnecessary blocking**, or even **deadlocks**.

#### 🔑 Using `signalAll()` ensures:
- Any thread that **can proceed**, does.
- It's a **safer choice** in mixed-waiting scenarios (like this one).

> In production systems, a better approach is to use **two separate conditions**:
> - `notFull` for producers.
> - `notEmpty` for consumers.

---

### 3. What is a circular buffer and why use it?

A circular buffer:
- Reuses array space by wrapping around.
- Efficiently supports FIFO behavior in a fixed-size array.
- Eliminates the need to shift elements.

In this queue:
```java
if (tail == capacity) tail = 0;
if (head == capacity) head = 0;
```

This logic resets indexes when they reach the array’s end, making it circular.

---

## 🚀 Possible Improvements

- Replace `condition` with **two Conditions**: `notFull` and `notEmpty`.
- Add `peek()` or `size()` methods.
- Add time-based operations (`offer()`, `poll()` with timeout).
- Parameterize the queue to support any type (e.g., `BlockingQueue<T>`).

---

## 🧠 Summary

This `BlockingQueue` implementation shows:
- Core concurrency in Java.
- How `Lock` and `Condition` give more power than `synchronized`.
- The **correct use of `signalAll()`** to avoid subtle bugs in concurrent environments.

Perfect for interview prep, real-time systems, or just brushing up on threading fundamentals. ✨
```

---

Let me know if you’d like me to convert this into a proper GitHub-style README file (`.md` file), or generate code to use two separate `Condition`s (`notEmpty` and `notFull`) for even cleaner producer-consumer signaling.