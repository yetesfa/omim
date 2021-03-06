#pragma once

#include "base/macros.hpp"

#include "std/cstdint.hpp"
#include "std/unique_ptr.hpp"

namespace my
{
class RefCounted
{
public:
  virtual ~RefCounted() = default;

  inline void IncRef() noexcept { ++m_refs; }
  inline uint64_t DecRef() noexcept { return --m_refs; }
  inline uint64_t NumRefs() const noexcept { return m_refs; }

protected:
  RefCounted() noexcept = default;

  uint64_t m_refs = 0;

  DISALLOW_COPY_AND_MOVE(RefCounted);
};

template <typename T>
class RefCountPtr
{
public:
  RefCountPtr() noexcept = default;

  explicit RefCountPtr(T * p) noexcept : m_p(p)
  {
    if (m_p)
      m_p->IncRef();
  }

  explicit RefCountPtr(unique_ptr<T> p) noexcept : RefCountPtr(p.release()) {}

  RefCountPtr(RefCountPtr const & rhs) { *this = rhs; }

  RefCountPtr(RefCountPtr && rhs) { *this = move(rhs); }

  ~RefCountPtr() { Reset(); }

  RefCountPtr & operator=(unique_ptr<T> p)
  {
    Reset();

    m_p = p.release();
    if (m_p)
      m_p->IncRef();

    return *this;
  }

  RefCountPtr & operator=(RefCountPtr const & rhs)
  {
    if (this == &rhs)
      return *this;

    Reset();
    m_p = rhs.m_p;
    if (m_p)
      m_p->IncRef();

    return *this;
  }

  RefCountPtr & operator=(RefCountPtr && rhs)
  {
    if (this == &rhs)
      return *this;

    Reset();
    m_p = rhs.m_p;
    rhs.m_p = nullptr;

    return *this;
  }

  void Reset()
  {
    if (!m_p)
      return;

    if (m_p->DecRef() == 0)
      delete m_p;
    m_p = nullptr;
  }

  T * Get() noexcept { return m_p; }
  T const * Get() const noexcept { return m_p; }

  T & operator*() { return *m_p; }
  T const & operator*() const { return *m_p; }

  T * operator->() noexcept { return m_p; }
  T const * operator->() const noexcept { return m_p; }

  inline operator bool() const noexcept { return m_p != nullptr; }

private:
  T * m_p = nullptr;
};
}  // namespace my
