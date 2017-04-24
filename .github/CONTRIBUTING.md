# Contributing to Novoda's Open Source projects

We encourage everyone inside and outside Novoda to contribute to the projects using Github's pull requests.

## Issuing a pull request

Github makes it really easy to create a pull request (PR) against a repo. Just fork it, implement your changes and create a pull request back to the original repo.

The PR should follow this format:

  * The title of the PR should be a short sentence explaining the fix
  * The PR description must contain at least two sections:
    1. **The problem**: Explain what's the bug you're trying to solve or the missing feature you're trying to add with this PR.
    2. **The solution**: Explain the fix or feature you've implemented in the PR. If this PR caused any UI change, then you should include screenshots or gifs showing how it looked before and after the change. See https://guides.github.com/features/mastering-markdown/ to create great looking markdown tables for showing your UI changes.
    3. Feel free to include funny memes or gifs ;)


## Writing tests

When you issue a PR, please take some time to consider writing tests for the issue. For example if you're solving a bug, you could write the test that reproduces the bug first, then fix the issue. This makes sure the bug doesn't come back later.

Also make sure the project builds and all the tests pass before creating the PR.

A non tested PR will not be merged back.


## Code formatting

We use a pretty standard Java code formatting:

  * Use spaces instead of tabs.
  * Indenting as explained here: http://en.wikipedia.org/wiki/Indent_style#Variant:_1TBS
  * Use curly braces for everything, even for one line `if`, `for`, etc. statements.
  * One line of white spaces between methods.
  * One space before parenthesis, curly braces, equals, etc. Such as:
    ```java
    if (value == 2) {
        value = 3;
    }
    ```

To make it easier we've made public the IDE settings we use internally so that you can import them if you want: https://github.com/novoda/novoda/tree/master/ide-settings
