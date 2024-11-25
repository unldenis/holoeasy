# Changelog

## v4.3.2

*November 21, 2024*

<br/>

<ins>Minor</ins> non-breaking changes:

- ProtocolLib.jar included in the repository to avoid other errors with the repository.

---

## v4.3.0

*November 21, 2024*

<br/>

<ins>Major</ins> non-breaking changes:

- Now when creating a hologram use the `blockLine` method for the blocks, and the `itemLine` method for the dropped items.

<ins>Minor</ins> non-breaking changes:

- ILine interface renamed to `Line`
- Line interface now exposes only usable methods, not internal ones.
- `BlockLineModifiers` now removed.

---

## v4.2.1

*November 4, 2024*

<br/>

<ins>Major</ins> non-breaking changes:

- Project uses Gradle instead of Maven.

<ins>Minor</ins> non-breaking changes:

- PacketEvents hologram lines now support **colors**.

---

## v4.2.0

*November 3, 2024*

<br/>

<ins>Major</ins> non-breaking changes:

- Added support for **PacketEvents** library. Tested in 1.20.2.

---

## v4.1.0

*October 6, 2024*

<br/>

<ins>Minor</ins> changes:

- Pool now support generics holograms.

---

## v4.0.0

*October 5, 2024*

<br/>

This update introduces lots of breaking changes.

<ins>Major</ins> breaking changes:

- Functional approach is dropped in favor of typesafe Hologram classes.
- Added support for holograms serialization and deserialization.

<ins>Minor</ins> breaking changes:

- `TextLineModifiers` and `BlockLineModifiers` for line customization.
- Plugin is now set once.