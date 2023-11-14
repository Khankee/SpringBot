package lk.poools.top.springbot.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pool {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Calendar calendar;
    private String data;
}
