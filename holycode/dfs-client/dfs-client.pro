# #####################################################################
# Automatically generated by qmake (2.01a) ?? ???. 14 16:25:16 2010
# #####################################################################
CONFIG += qtestlib
TEMPLATE = app
TARGET = 
DEPENDPATH += . \
    controller \
    exceptions \
    logger \
    model \
    protocol \
    tests \
    view \
    model/main \
    protocol/network \
    protocol/xmlBuilder \
    protocol/xmlParser
INCLUDEPATH += . \
    controller \
    view \
    model/main \
    model \
    exceptions \
    protocol/xmlParser \
    protocol \
    protocol/network \
    protocol/xmlBuilder \
    logger \
    tests

# Input
HEADERS += controller/controller.h \
    exceptions/filelistexception.h \
    exceptions/mainmodelexception.h \
    logger/logger.h \
    model/filedescription.h \
    model/filelist.h \
    model/requeststate.h \
    model/state.h \
    model/user.h \
    protocol/connector.h \
    tests/test.h \
    view/addfiledialog.h \
    view/fail.h \
    view/inputdialog.h \
    view/logindialog.h \
    view/mainwindow.h \
    view/mdichild.h \
    view/numericwidget.h \
    view/opendialog.h \
    model/main/mainmodel.h \
    protocol/network/tcpconnector.h \
    protocol/xmlBuilder/xmlbuilder.h \
    protocol/xmlParser/downloadhandler.h \
    protocol/xmlParser/listrequesthandler.h \
    protocol/xmlParser/loginhandler.h \
    protocol/xmlParser/newhandler.h \
    protocol/xmlParser/parser.h \
    protocol/xmlParser/searchhandler.h \
    pdfGenerator/pdfGenerator.h \
    protocol/xmlParser/deletehandler.h \
    protocol/xmlParser/pastehandler.h \
    view/confirmation.h
FORMS += view/addfiledialog.ui \
    view/fail.ui \
    view/inputdialog.ui \
    view/logindialog.ui \
    view/mainwindow.ui \
    view/opendialog.ui \
    view/confirmation.ui
SOURCES += main.cpp \
    controller/controller.cpp \
    logger/logger.cpp \
    model/filelist.cpp \
    protocol/connector.cpp \
    tests/test.cpp \
    view/logindialog.cpp \
    view/mainwindow.cpp \
    view/mdichild.cpp \
    model/main/mainmodel.cpp \
    protocol/network/tcpconnector.cpp \
    protocol/xmlBuilder/xmlbuilder.cpp \
    protocol/xmlParser/downloadhandler.cpp \
    protocol/xmlParser/listrequesthandler.cpp \
    protocol/xmlParser/loginhandler.cpp \
    protocol/xmlParser/newhandler.cpp \
    protocol/xmlParser/parser.cpp \
    protocol/xmlParser/searchhandler.cpp \
    pdfGenerator/pdfGenerator.cpp \
    protocol/xmlParser/deletehandler.cpp \
    protocol/xmlParser/pastehandler.cpp
QT += xml
QT += network