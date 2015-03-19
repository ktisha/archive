#ifndef STATE_H
#define STATE_H
#include <QString>
// possible states of system
enum State {
    SENDFILE, UPDATE, LISTREQUEST, GETFILE,
    LOGIN, SEARCH, NEW, OPEN, DELETEFILE,
    DOWNLOAD, ADD, PASTE, DELETEFLIST, ERROR,
};

static QString stateStrings[] = {"Send file", "update", "list request", "get file",
                             "login", "search", "new", "open", "delete file",
                             "download", "add", "paste", "delete filelist", "error",
                             0 };
inline QString stateToStr(State st) {
  return stateStrings[(int)st];
}

#endif // STATE_H
