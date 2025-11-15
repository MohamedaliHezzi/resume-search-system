package model;

public enum TypeDocument {
	PDF,
    DOC;
    public static TypeDocument fromFileExtension(String extension) {
        if (extension != null) {
            switch (extension.toLowerCase()) {
                case "doc":
                case "docx":
                    return DOC;
                case "pdf":
                    return PDF;
                default:
                    return null; 
            }
        }
        return null; 
    }
}
