package searchengine.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "pages")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "INT", nullable = false)
    private int id;

//    @JoinColumn(name = "site_id")
//    @Column(columnDefinition = "INT", nullable = false)
//    private Site site;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String path;

    @Column(columnDefinition = "INT", nullable = false)
    private int statusCode;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;
}
