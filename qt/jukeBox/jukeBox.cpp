#include <QtGui>
#include <QString>
#include "jukeBox.h"

JukeBox::JukeBox() {
    setupUi(this);
    connect(openButton, SIGNAL(clicked()), this, SLOT(openClicked()));
    connect(saveButton, SIGNAL(clicked()), this, SLOT(saveClicked()));
    this->show();
}

void JukeBox::openClicked() {
    QString f = QFileDialog::getOpenFileName(this, "Open", "*.xml");
    if(! f.isEmpty())
        loadFile(f);
}

void JukeBox::loadFile(const QString & str) {
    QDomDocument domDoc;
    QFile file(str);
    if (file.open(QIODevice::ReadOnly)) {
        domDoc.setContent(&file);
        QDomElement domElement = domDoc.documentElement();
        analyse(domElement);
        file.close();
    }
}

void JukeBox::analyse(const QDomNode& node) {
    QDomNode domNode = node.firstChild();
    static const QString artistTag = "artist"; 
    static const QString albumTag = "album";

    while (!domNode.isNull()) {
        if (domNode.isElement()) {
            QDomElement domElement = domNode.toElement();
            if (!domElement.isNull()) {
                if (domElement.tagName() == artistTag) {
                    QDomNode attribute = domNode.attributes().namedItem("value");
                            artistEdit->insert(attribute.nodeValue());
                } else if (domElement.tagName() == albumTag) {
                    QDomNode attribute = domNode.attributes().namedItem("value");
                    albumEdit->insert(attribute.nodeValue());
                }
            }
        }
        analyse(domNode);
        domNode = domNode.nextSibling();
    }
}

void JukeBox::saveClicked() {
    QString f = QFileDialog::getSaveFileName(this, "Save", "*.xml");
    saveFile(f);
}

void JukeBox::saveFile(const QString & str) {
    QDomDocument* doc = new QDomDocument();
    QFile f(str);
    entryToDom(*doc);
    if (!f.open(QIODevice::WriteOnly)) {
        qDebug() << "cannot open file for writing\n";
    }
    QTextStream out(&f);
    doc->save(out, 4);
}

void JukeBox::entryToDom(QDomDocument& doc) {
    QDomNode xmlNode = doc.createProcessingInstruction("xml",
                                            "version=\"1.0\" encoding=\"utf-8\"");
    doc.insertBefore(xmlNode, doc.firstChild());
    QDomElement root = doc.createElement("entry");
    doc.appendChild(root);

    const QString artistTag = "artist";
    const QString albumTag = "album";

    QDomElement artist = doc.createElement(artistTag);
    artist.setAttribute("value", artistEdit->text());
    root.appendChild(artist);

    QDomElement album = doc.createElement(albumTag);
    album.setAttribute("value", albumEdit->text());
    root.appendChild(album);
}

