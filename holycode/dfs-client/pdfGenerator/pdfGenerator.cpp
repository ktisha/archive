#include <QTextEdit>
#include <QPrinter>
#include <QFile>
#include <QTextStream>
#include <QMap>

#include "pdfGenerator.h"
#include "model/filelist.h"


QString pdfGenerate(const QString & fileName, const FileList & fileList) {
    // tuning printer
    QPrinter printer(QPrinter::HighResolution);
    printer.setOutputFormat(QPrinter::PdfFormat);

    printer.setOutputFileName(fileName);

    // read templates
    QString html;
    QString head;
    QString body;
    QString end;

    QString templateDir;
    if (fileList.getIsSearch()) {
        templateDir = "data/template/search/";
    } else {
        templateDir = "data/template/";
    }

    QFile file1(templateDir + "head.xml");
    if (!file1.open(QIODevice::ReadOnly | QIODevice::Text))
             return "Can't open 'template head.xml'";

    QTextStream in(&file1);
    while (!in.atEnd()) {
        head = in.readAll();
    }

    QFile file2(templateDir + "body.xml");
    if (!file2.open(QIODevice::ReadOnly | QIODevice::Text))
        return "Can't open 'template body.xml'";

    in.setDevice(&file2);
    while (!in.atEnd()) {
        body = in.readAll();
    }

    QFile file3(templateDir + "end.xml");
    if (!file3.open(QIODevice::ReadOnly | QIODevice::Text))
        return "Can't open 'template end.xml'";

    in.setDevice(&file3);
    while (!in.atEnd()) {
        end = in.readAll();
    }

    // fill templates
    if (fileList.getIsSearch()) {
        head.replace("{{ head }}", "Search result for '" + fileList.getName() + "'");
        html.append(head);
        QListIterator<FileDescription> it = fileList.getIterator();
        int i = 0;
        while (it.hasNext()) {
                const FileDescription & fileDescr(it.next());
                QString bodyFill = body;
                bodyFill.replace("{{ # }}", QString::number(++i));
                bodyFill.replace("{{ name }}", fileDescr.getName());
                bodyFill.replace("{{ size }}", QString::number(fileDescr.getSize()));
                bodyFill.replace("{{ copy }}", QString::number(fileDescr.getCopy()));
                bodyFill.replace("{{ filelist }}", fileDescr.getFileList());
                html.append(bodyFill);
        }

    } else {
        head.replace("{{ head }}", "Filelist '" + fileList.getName() + "'");
        html.append(head);
        QListIterator<FileDescription> it = fileList.getIterator();
        int i = 0;
        while (it.hasNext()) {
                const FileDescription & fileDescr(it.next());
                QString bodyFill = body;
                bodyFill.replace("{{ # }}", QString::number(++i));
                bodyFill.replace("{{ name }}", fileDescr.getName());
                bodyFill.replace("{{ size }}", QString::number(fileDescr.getSize()));
                bodyFill.replace("{{ copy }}", QString::number(fileDescr.getCopy()));
                html.append(bodyFill);
        }
    }

    html.append(end);

    //qDebug() << html;
    // print html to printer
    QTextEdit(html).print(&printer);
    return "";
}
